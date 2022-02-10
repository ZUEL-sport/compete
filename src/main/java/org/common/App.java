package org.common;

import cn.fabrice.common.constant.BaseConstants;
import cn.fabrice.jfinal.ext.cros.interceptor.CrossInterceptor;
import cn.fabrice.jfinal.interceptor.ParaValidateInterceptor;
import cn.fabrice.jfinal.plugin.ValidationPlugin;
import cn.fabrice.kit.json.FJsonFactory;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.wall.WallFilter;
import com.jfinal.config.*;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import com.jfinal.server.undertow.UndertowServer;
import com.jfinal.template.Engine;
import org.common.module._MappingKit;
import org.game.GameController;
import org.user.UserController;

/**
 * @author Administrator
 */
public class App extends JFinalConfig {
    public static  void main(String[]args){
        UndertowServer.start(App.class);
    }

    /**
     * 1.使用JFinal里面的PropKit进行基本设置
     * 2.直接进行设置,设置成true表示进入开发模式，此时路径就比较的
     * me.setDevMode(true);
     */
    @Override
    public void configConstant(Constants me) {
        PropKit.use("base_config.properties");
        me.setDevMode(PropKit.getBoolean("devMode",true));
        //设置日志
        me.setToSlf4jLogFactory();
        //允许AOP注入
        me.setInjectDependency(true);
        // 配置文件上传下载路径，没有配置的话就是upload和download文件夹
        me.setBaseUploadPath("src/file");
        me.setBaseDownloadPath("file/download");
        // 设置上传文件的最大大小
        me.setMaxPostSize(20*1024*1024);
        // 前后端以驼峰的形式传递数据
        me.setJsonFactory(new FJsonFactory());
    }

    @Override
    public void configRoute(Routes me) {
        me.scan("org.");
        /*将UserController的路径设为user,使用user地址就可以调用*/
        me.add("/user", UserController.class);
        me.add("/game", GameController.class);
    }

    @Override
    public void configEngine(Engine me) {

    }

    @Override
    public void configPlugin(Plugins me) {
        // 读取数据库配置文件
        Prop prop = PropKit.use("db_config.properties");
        // 使用druid数据库连接池进行操作
        DruidPlugin druidPlugin = new DruidPlugin(prop.get("db_url").trim(),
                prop.get("db_user").trim(), prop.get("db_password").trim());
        // 加强数据库安全
        WallFilter wallFilter = new WallFilter();
        wallFilter.setDbType("mysql");
        druidPlugin.addFilter(wallFilter);
        // 添加 StatFilter 才会有统计数据
        druidPlugin.addFilter(new StatFilter());

        druidPlugin.setMaxActive(20);
        druidPlugin.setMinIdle(5);
        druidPlugin.setInitialSize(5);
        druidPlugin.setConnectionInitSql("set names utf8mb4");
        druidPlugin.setValidationQuery("select 1 from dual");
        me.add(druidPlugin);
        // 配置数据库活动记录插件
        ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
        Engine engine = arp.getEngine();
        // 上面的代码获取到了用于 sql 管理功能的 Engine 对象，接着就可以开始配置了
        engine.setToClassPathSourceFactory();
        engine.addSharedMethod(StrKit.class);
        //设置是否显示sql文件
        arp.setShowSql(PropKit.getBoolean("dev", true));
        //设置sql文件存储的基础路径，此段代码表示设置为classpath目录
        arp.setBaseSqlTemplatePath(null);
        //所有模块sql
        arp.addSqlTemplate("sql/all.sql");
        // 对应关系映射（需等待运行Generator语句生成后替换）
        _MappingKit.mapping(arp);
        me.add(arp);
        //添加规则校验插件
        me.add(new ValidationPlugin());
        //增加缓存插件
        me.add(new EhCachePlugin());


    }

    @Override
    public void configInterceptor(Interceptors me) {
        // 首先添加跨域拦截器
        me.add(new CrossInterceptor(BaseConstants.TOKEN_NAME,true));
        // 最后添加参数校验拦截器
        me.add(new ParaValidateInterceptor());
    }

    @Override
    public void configHandler(Handlers me) {

    }

    @Override
    public void onStart() {
        System.out.println("app starting......");;
    }

    @Override
    public void onStop() {
        System.out.println("app stopping......");;
    }
}
