package java.com.zy.generator;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.beetl.core.BeetlKit;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;

import java.beans.PropertyVetoException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.*;

/**
 * 简易辅助开发代码生成器
 * 描述：根据表，生成对应的Entity类、Model类、Service类、DAO类、Controller类，
 * 不包含业务处理逻辑，考虑到开发的业务个性化，通用的生成意义不是太大，只做辅助开发
 */
public class CodeGenerator {
    public static String jdbcDriverClass = "com.mysql.jdbc.Driver";
    public static String jdbcUrl = "jdbc:mysql://localhost:3306/points?characterEncoding=utf8&serverTimezone=UTC";
    public static String jdbcUsername = "root";
    public static String jdbcPassword = "123456";
    public static String jdbcDbName = "tables";//数据库名
    public static String jdbcTablePrefix = "table_";//表名前缀

    public static String childFolder = "xxx/xxx";


    // 生成的包和类所在的源码根目录，比如src或者是weiXin
    public static String srcFolder = "src/main/java";
    public static String srcMapperFolder = "src/main/resources";

    /**
     * 生成的文件存放的包，公共基础包
     * 描述：比如
     * platform所在的包就是com.rr.platform
     * weixin所在的包就是com.rr.weixin
     */
    public static String packageBase = "com.anyangdp.tool";

    public static ComboPooledDataSource dataSource() throws PropertyVetoException {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setDriverClass(jdbcDriverClass);
        dataSource.setJdbcUrl(jdbcUrl);
        dataSource.setUser(jdbcUsername);
        dataSource.setPassword(jdbcPassword);
        return dataSource;
    }

    /**
     * 循环生成文件
     */
    public static void main(String[] args) throws PropertyVetoException {
        ComboPooledDataSource dataSource = dataSource();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        List<Map<String, Object>> l = jdbcTemplate.queryForList("show table status like '" + jdbcTablePrefix + "%'");
        for (Map<String, Object> m : l) {
            // 表名
            String tableName = m.get("Name").toString();//r.getStr("name");
            // 类名
            String className = className(tableName);
            // 类名首字母小写
            String classNameSmall = StringUtil.firstCharToLowerCase(className);
            //表注释
            String tableComment = m.get("Comment").toString();//r.getStr("comment");

            Map<String, Object> paraMap = new HashMap<String, Object>();
            paraMap.put("package", packageBase);
            paraMap.put("className", className);
            paraMap.put("tableName", tableName);
            paraMap.put("tableComment", tableComment);
            paraMap.put("classNameSmall", classNameSmall);
            String baseFilePath = System.getProperty("user.dir") + "/" + childFolder + "/" + srcFolder + "/" + packageBase.replace(".", "/");
            List<Map<String, Object>> fields = jdbcTemplate.queryForList("show full fields from " + jdbcDbName + "." + tableName);
            for (Map<String, Object> f : fields) {
                String fieldName = f.get("Field").toString().toLowerCase();//f.getStr("field");
                f.put("fieldName", fieldName);
                fieldName = StringUtil.tableName_2_modelName(jdbcTablePrefix, fieldName);
                f.put("fieldNameUppercase", fieldName);
                fieldName = StringUtil.firstCharToLowerCase(fieldName);
                f.put("field", fieldName);
                String fieldType = f.get("Type").toString();//f.getStr("type");
                fieldType = StringUtil.getFieldTypeToJavaType(fieldType);
                f.put("type", fieldType);
            }
            paraMap.put("fields", fields);

            createTemplete(className, paraMap, baseFilePath);
        }
        dataSource.close();
    }

    private static void createTemplete(String className, Map<String, Object> paraMap, String baseFilePath) {
        //Entity
        createFileByTemplate("DOBJ.txt", paraMap, baseFilePath + "/domain/dobj/" + className + "DO.java");
        //DTO
        createFileByTemplate("DTO.txt", paraMap, baseFilePath + "/domain/dto/" + className + "DTO.java");
        //DAO
        createFileByTemplate("DAO.txt", paraMap, baseFilePath + "/mapper/" + className + "Mapper.java");
        //Service
        createFileByTemplate("Service.txt", paraMap, baseFilePath + "/service/" + className + "Service.java");
        //DefaultService
        createFileByTemplate("DefaultService.txt", paraMap, baseFilePath + "/service/impl/" + className + "ServiceImpl.java");
        //Controller
        createFileByTemplate("Controller.txt", paraMap, baseFilePath + "/controller/" + className + "Controller.java");
        //mapper
        String baseMapperFilePath = System.getProperty("user.dir") + "/" + childFolder + "/" + srcMapperFolder;
        createFileByTemplate("MapperXML.txt", paraMap, baseMapperFilePath + "/mapper/" + className + "ExtMapper.xml");
    }

    private static String className(String tableName) {
        List<String> ts = new ArrayList<>();
        if (tableName.contains("_")) {
            ts = Arrays.asList(tableName.split("_"));
        } else {
            ts.add(tableName);
        }
        String className = "";
        for (String s : ts) {
            if (!StringUtils.isEmpty(s)) {
                className += StringUtil.firstCharToUpperCase(s);
            }
        }
        return className;
    }

    /**
     * 根据具体模板生成文件
     *
     * @param templateFileName
     * @param paraMap
     * @param filePath
     */
    public static void createFileByTemplate(String templateFileName, Map<String, Object> paraMap, String filePath) {
        try {
            InputStream controllerInputStream = CodeGenerator.class.getResourceAsStream("/" + templateFileName);
            int count = 0;
            while (count == 0) {
                count = controllerInputStream.available();
            }

            byte[] bytes = new byte[count];
            int readCount = 0; // 已经成功读取的字节的个数
            while (readCount < count) {
                readCount += controllerInputStream.read(bytes, readCount, count - readCount);
            }

            String template = new String(bytes);

            String javaSrc = BeetlKit.render(template, paraMap);

            File file = new File(filePath);
            if(!file.exists()) {
                File fileP = new File(file.getParent());
                if (!fileP.exists()) {
                    fileP.mkdirs();
                }
                BufferedWriter output = new BufferedWriter(new FileWriter(file));
                output.write(javaSrc);
                output.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
