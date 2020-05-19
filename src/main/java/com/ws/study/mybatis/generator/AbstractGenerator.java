package com.ws.study.mybatis.generator;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

/**
 * Description：
 *
 * @author wangsong
 * @version 1.0
 * @since 5/18/2020 1:50 PM
 */
public abstract class AbstractGenerator {

    protected GeneratorConfig config;

    public AbstractGenerator(GeneratorConfig config) {
        this.config = config;
    }

    public abstract String[] getTableNames();

    public void generate() {
        AutoGenerator mpg = new AutoGenerator();
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.setGlobalConfig(this.config.getGlobalConfig());
        mpg.setDataSource(this.config.getDataSourceConfig());
        mpg.setPackageInfo(this.config.getPackageConfig());
        mpg.setCfg(this.config.getInjectionConfig());
        mpg.setTemplate(this.config.getTemplateConfig());

        // Setting table names
        StrategyConfig strategyConfig = this.config.getStrategyConfig();
        strategyConfig.setInclude(getTableNames());
        mpg.setStrategy(strategyConfig);

        mpg.execute();
    }
}
