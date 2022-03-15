
import com.sun.codemodel.JCodeModel;
import org.jsonschema2pojo.*;
import org.jsonschema2pojo.rules.RuleFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.net.URL;
public class GeneratePojo {
    public static void main(String[] args) {

        String packageName="com.risk";

        try {
            ClassPathResource res = new ClassPathResource("test.json");
            File inputJson= new File(String.valueOf(res.getFile()));

            File outputPojoDirectory=new File("."+File.separator+"convertedPojo");

            outputPojoDirectory.mkdirs();

            convert2JSON(inputJson.toURI().toURL(), outputPojoDirectory, packageName, inputJson.getName().replace("json", ""));

        } catch (IOException e) {

            // TODO Auto-generated catch block

            System.out.println("Encountered issue while converting to pojo: "+e.getMessage());

            e.printStackTrace();

        }

    }

    public static void  convert2JSON(URL inputJson, File outputPojoDirectory, String packageName, String className) throws IOException{

        JCodeModel codeModel = new JCodeModel();

        URL source = inputJson;

        GenerationConfig config = new DefaultGenerationConfig() {

            @Override

            public boolean isGenerateBuilders() { // set config option by overriding method

                return true;

            }

            public SourceType getSourceType(){

                return SourceType.JSON;

            }

        };

        SchemaMapper mapper = new SchemaMapper(new RuleFactory(config, new Jackson2Annotator(config), new SchemaStore()), new SchemaGenerator());

        mapper.generate(codeModel, className, packageName, source);

        codeModel.build(outputPojoDirectory);

    }
}
