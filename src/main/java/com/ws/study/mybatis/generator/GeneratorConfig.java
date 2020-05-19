package com.ws.study.mybatis.generator;

import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.FileType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import lombok.Data;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Description：
 *
 * @author wangsong
 * @version 1.0
 * @since 5/18/2020 1:49 PM
 */
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
