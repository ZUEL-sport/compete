package org.common.module;

import cn.fabrice.kit.jfinal.GeneratorModelKit;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;

/**
 * 数据库生成
 * @author Administrator
 */
public class Generator {
    public static void main(String[]args){
        Prop prop = PropKit.use("db_config.properties");
        GeneratorModelKit.generate(prop.get("db_url").trim(), prop.get("db_user").trim(),
                prop.get("db_password").trim(),"org.common.module");
    }
}
