import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import com.moyao.generator.GenericMyBatisGenerator;

public class MybatisGenerator {

    public static void main(String[] args)  {
        List<String> warnings = new ArrayList<String>();
        try {
            String genCfg = "mbgConfiguration.xml";
            File configFile = new File(MybatisGenerator.class.getResource(genCfg).getFile());
            ConfigurationParser cp = new ConfigurationParser(warnings);
            Configuration config = cp.parseConfiguration(configFile);
            DefaultShellCallback callback = new DefaultShellCallback(true);
            GenericMyBatisGenerator myBatisGenerator = new GenericMyBatisGenerator(config, callback, warnings);
            myBatisGenerator.generate(null);
        }catch (Exception e){
            warnings.forEach( System.out::println);
            e.printStackTrace();
        }
    }
}
