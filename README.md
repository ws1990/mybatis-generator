# 一、概述
之前一直使用mybatis的自动代码生成，虽然也能基本满足要求，但是在实际使用过程中还是存在问题，比如：

* 自动生成的代码，每一个mapper，每一个service都会有大量的重复代码，重复代码不处理吧看着不舒服，处理的话呢，需要抽象出一个基础类，在spring环境下，对基础类以及子类初始化又显得不够优雅(参考该项目[shuzheng/zheng](https://github.com/shuzheng/zheng))；
* 在开发过程中，发现需要修改表结构，每次重新生成的话怎么指定哪些文件是需要覆盖的；
* 如何自定义模版文件，且支持自定义变量

带着这些问题，又重新研究了mybatis的好基友mybatis-plus，发现基本上可以完美解决以上面临的问题。

# 二、具体实现
## 2.1 pom文件依赖
```java
	<properties>
        <mybatis-plus.version>3.1.2</mybatis-plus.version>
    </properties>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.1.3.RELEASE</version>
    </parent>

    <dependencies>
        <!-- starter -->
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
            <version>${mybatis-plus.version}</version>
        </dependency>

        <!-- 数据库 -->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>6.0.6</version>
        </dependency>

        <!-- 自动生成代码 -->
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-generator</artifactId>
            <version>${mybatis-plus.version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-freemarker</artifactId>
        </dependency>

        <!-- 工具 -->
        <dependency>
            <groupId>com.google.guava</groupId>
            <artifactId>guava</artifactId>
            <version>25.0-jre</version>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.4</version>
        </dependency>
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-lang3</artifactId>
            <version>3.9</version>
        </dependency>
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-collections4</artifactId>
            <version>4.4</version>
        </dependency>
    </dependencies>
```

## 2.2 核心Config类
```java
@Data
public class GeneratorConfig {
    private static String projectPath = currentProjectPath();

    private DataSourceConfig dataSourceConfig;
    private GlobalConfig globalConfig;
    private PackageConfig packageConfig;
    private TemplateConfig templateConfig;
    private StrategyConfig strategyConfig;
    private InjectionConfig injectionConfig;

    private List<String> includeTableNames;

    public GeneratorConfig() {
        this.dataSourceConfig = new DataSourceConfig();
        this.globalConfig = initGlobalConfig();
        this.packageConfig = new PackageConfig();
        this.strategyConfig = initStrategyConfig();
        this.templateConfig = initTemplateConfig();
        this.injectionConfig = initInjectionConfig();
    }

    /**
     * 全局配置
     */
    private static GlobalConfig initGlobalConfig() {
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setOpen(false);
        gc.setEntityName("%sEntity");
        gc.setServiceName("%sService");
        // 默认允许覆盖文件
        gc.setFileOverride(true);

        return gc;
    }

    /**
     * 策略配置
     */
    private static StrategyConfig initStrategyConfig() {
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);

        return strategy;
    }

    /**
     * 模版配置
     * 优先扫描本项目中的templates目录
     */
    private static TemplateConfig initTemplateConfig() {
        TemplateConfig config = new TemplateConfig();
        // 不生成Controller
        config.setController(null);

        return config;
    }

    /**
     * 自定义配置
     */
    private static InjectionConfig initInjectionConfig() {
        // 自定义参数，在模版文件里通过${cfg.xxx}引用
        InjectionConfig config = new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = this.getMap();
                if (map == null) {
                    map = new HashMap<>();
                    this.setMap(map);
                }

                String datetime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                map.put("datetime", datetime);
            }
        };

        // 如果当前文件已经存在，则仅允许覆盖Entity
        IFileCreate fileCreate = (configBuilder, fileType, filePath) -> {
            if (new File(filePath).exists()) {
                if (configBuilder.getGlobalConfig().isFileOverride()) {
                    return FileType.ENTITY.equals(fileType);
                } else {
                    return false;
                }
            }

            return true;
        };
        config.setFileCreate(fileCreate);

        return config;
    }

    private static String currentProjectPath() {
        String projectCompletePath = Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResource("")).getPath();
        if (projectCompletePath.startsWith("/")) {
            projectCompletePath = projectCompletePath.replaceFirst("/", "");
        }

        return projectCompletePath.replace("/target/classes/", "");
    }
}
```

## 2.3 最终效果
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200519173935567.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dzNTc2MDIzMjE5,size_16,color_FFFFFF,t_70#pic_center)

# 三、问题是如何解决的
我们来看看文章开头提出的两个问题是如何解决的
## 3.1 解决重复代码的问题
这个从最终的结果来看，mybatis-plus自动给我们继承了一个基础类，完全不用我们自己操心。
## 3.2 解决重新生成时的文件覆盖问题
```java
		// 如果当前文件已经存在，则仅允许覆盖Entity
        IFileCreate fileCreate = (configBuilder, fileType, filePath) -> {
            if (new File(filePath).exists()) {
                if (configBuilder.getGlobalConfig().isFileOverride()) {
                    return FileType.ENTITY.equals(fileType);
                } else {
                    return false;
                }
            }

            return true;
        };
        config.setFileCreate(fileCreate);
```

## 3.3 支持自定义模版和自定义参数
1. 首先需要在resources目录下创建目录templates，然后将模版文件放进去，需要注意模版文件必须和TemplateEngine匹配。
2. 在模版文件里添加自定义参数，例如datetime，通过${cfg.datetime}的方式
```java
/**
 * Description:
 * ${table.comment!}
 *
 * @author ${author}
 * @since ${cfg.datetime}
 */
```
3. 在代码里配置自定义配置
```java
		// 自定义参数，在模版文件里通过${cfg.xxx}引用
        InjectionConfig config = new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = this.getMap();
                if (map == null) {
                    map = new HashMap<>();
                    this.setMap(map);
                }

                String datetime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                map.put("datetime", datetime);
            }
        };
```

# 四、参考
1. 所有代码都在个人github项目里[mybatis-generator](https://github.com/ws1990/mybatis-generator)
3. [mybatis-plus官网](https://mp.baomidou.com/guide/)
