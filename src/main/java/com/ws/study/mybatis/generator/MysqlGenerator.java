package com.ws.study.mybatis.generator;

import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Description：
 *
 * @author wangsong
 * @version 1.0
 * @since 5/18/2020 2:56 PM
 */
public class MysqlGenerator extends AbstractGenerator {

    public MysqlGenerator(GeneratorConfig config) {
        super(config);
    }

    @Override
    public String[] getTableNames() {
        if (CollectionUtils.isNotEmpty(this.config.getIncludeTableNames())) {
            return this.config.getIncludeTableNames().toArray(new String[0]);
        }

        DataSourceConfig dataSourceConfig = this.config.getDataSourceConfig();
        DataSource dataSource = DataSourceBuilder.create()
                //如果不指定类型，那么默认使用连接池，会存在连接不能回收而最终被耗尽的问题
                .type(DriverManagerDataSource.class)
                .driverClassName(dataSourceConfig.getDriverName())
                .url(dataSourceConfig.getUrl())
                .username(dataSourceConfig.getUsername())
                .password(dataSourceConfig.getPassword())
                .build();
        JdbcOperations jdbcOperations = new JdbcTemplate(dataSource);

        String database = getDatabaseFromUrl();
        //该sql语句目前支持mysql
        String sql = "SELECT table_name FROM INFORMATION_SCHEMA.TABLES WHERE table_schema = '" + database + "'";
        String[] tablePrefixArray = this.config.getStrategyConfig().getTablePrefix();
        if (tablePrefixArray != null && tablePrefixArray.length == 1) {
            String tablePrefix = tablePrefixArray[0];
            if (StringUtils.isNotEmpty(tablePrefix)) {
                sql += " and (table_name like '" + tablePrefix + "_%')";
            }
        }

        List<String> tableNames = jdbcOperations.query(sql, SingleColumnRowMapper.newInstance(String.class));
        return ListUtils.emptyIfNull(tableNames).toArray(new String[0]);
    }

    private String getDatabaseFromUrl() {
        String url = this.config.getDataSourceConfig().getUrl();
        Pattern pattern = Pattern.compile("jdbc:mysql://.*/(.*)\\?.*");
        Matcher m = pattern.matcher(url);
        String str = "";
        if (m.find()) {
            str = m.group(1);
        }

        return str;
    }

}
