package com.ws.study.mybatis.generator;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

/**
 * Description：
 *
 * @author wangsong
 * @version 1.0
 * @since 5/18/2020 1:46 PM
 */
public class GeneratorBuilder {
    private final GeneratorConfig config = new GeneratorConfig();

    public static GeneratorBuilder getInstance() {
        return new GeneratorBuilder();
    }

    public GeneratorBuilder setUrl(String url) {
        this.config.getDataSourceConfig().setUrl(url);
        return this;
    }

    public GeneratorBuilder setDriverName(String driverName) {
        this.config.getDataSourceConfig().setDriverName(driverName);
        return this;
    }

    public GeneratorBuilder setUsername(String username) {
        this.config.getDataSourceConfig().setUsername(username);
        return this;
    }

    public GeneratorBuilder setPassword(String password) {
        this.config.getDataSourceConfig().setPassword(password);
        return this;
    }


    public GeneratorBuilder setAuthor(String author) {
        this.config.getGlobalConfig().setAuthor(author);
        return this;
    }

    public GeneratorBuilder setPackageName(String packageName) {
        this.config.getPackageConfig().setParent(packageName);
        return this;
    }

    public GeneratorBuilder setTablePrefix(String tablePrefix) {
        this.config.getStrategyConfig().setTablePrefix(tablePrefix);
        return this;
    }

    public GeneratorBuilder setIncludeTableNames(String... tableNames) {
        this.config.setIncludeTableNames(Lists.newArrayList(tableNames));
        return this;
    }

    public void generate() {
        if (isMysql()) {
            new MysqlGenerator(this.config).generate();
        } else {
            throw new RuntimeException("Db is not supported or url is error.");
        }
    }

    private boolean isMysql() {
        String url = this.config.getDataSourceConfig().getUrl();
        return StringUtils.isNotEmpty(url) && url.startsWith("jdbc:mysql://");
    }
}
