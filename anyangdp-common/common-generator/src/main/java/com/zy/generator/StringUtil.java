package com.zy.generator;


import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil{
	/**
	 * 首字母变小写
	 */
	public static String firstCharToLowerCase(String str) {
		char firstChar = str.charAt(0);
		if (firstChar >= 'A' && firstChar <= 'Z') {
			char[] arr = str.toCharArray();
			arr[0] += ('a' - 'A');
			return new String(arr);
		}
		return str;
	}
	
	/**
	 * 首字母变大写
	 */
	public static String firstCharToUpperCase(String str) {
		char firstChar = str.charAt(0);
		if (firstChar >= 'a' && firstChar <= 'z') {
			char[] arr = str.toCharArray();
			arr[0] -= ('a' - 'A');
			return new String(arr);
		}
		return str;
	}
	/** 
	 * 表名转模型名
	 */
	public static String tableName_2_modelName(String jdbcTablePrefix,String tableName) {
		String[] tableNameArr = tableName.replace(jdbcTablePrefix, "").split("_");
		StringBuilder modelName = new StringBuilder();
		for (int j = 0; j < tableNameArr.length; j++) 
			modelName.append(firstCharToUpperCase(tableNameArr[j]));
		return modelName.toString();
	}
	/** 
	 * 模型名转表名
	 */
	public static String modelName_2_tableName(String jdbcTablePrefix,String modelName) {
		String tableName = modelName.replaceAll("[A-Z]", "_$0");
		tableName = tableName.substring(1,tableName.length()).toLowerCase();
		return jdbcTablePrefix + tableName;
	}
	
	/**
	 * 从数据库字段类型，提取类型和长度
	 * 例如 varchar(20)
	 * @param typeFromDb
	 * @return varchar 20
	 */
	public static String[] getFieldTypeLength(String typeFromDb){
		Pattern p = Pattern.compile("(\\w+)\\((.+)\\)");
		Matcher m = p.matcher(typeFromDb);
		ArrayList<String> strArr = new ArrayList<String>();
		while (m.find()) {			
			for (int i = 1; i < m.groupCount()+1; i++) {
				strArr.add(m.group(i));
			}
		}
		if (strArr.size()==0)
			return new String[]{typeFromDb,""};
		else{
			String[] result = new String[strArr.size()];
			result = (String[]) strArr.toArray(result);
			return result;
		}
	}
	
	/**
	* @Description 从数据库字段类型，提取类型和长度 并还回java类型
	* @param typeFromDb  varchar(500)返回String int返回Integer
	* @return 
	* @throws
	 */
	public static String getFieldTypeToJavaType(String typeFromDb){
		String[] fieldTypeLength = getFieldTypeLength(typeFromDb);
		String type = fieldTypeLength[0];
		String length = fieldTypeLength[1];
		String result = null;
		switch (type) {
		case "bigint":
			result = "Long";
			break;
		case "int":
				result = "Integer";
			break;
		case "bit":
			result = "Boolean";
			break;
		case "tinyint":
			if("1".equals(length)){
				result = "Boolean";
			}else{
				result = "Integer";
			}
			break;
		case "varchar":
			result = "String";
			break;
		case "date":
			result = "Date";
		case "datetime":
			result = "Date";
			break;
		case "timestamp":
			result = "Date";
			break;
		case "decimal":
			result = "Double";
			break;
		case "double":
			result = "Double";
			break;
		case "text":
			result = "String";
		case "char":
			result = "String";
			break;
		case "blob":
			result = "String";
			break;
		}
		return result;
	}
	
	public static String implode(ArrayList<String> strArr, String separator){
		String[] result = new String[strArr.size()];
		result = (String[]) strArr.toArray(result);
		return implode(result,separator);
	}
	public static String implode(String[] strArr, String separator){
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < strArr.length; i++) {
			if(i!=0)
				result.append(separator);
			result.append(strArr[i]);
		}
		return result.toString();
	}
	/** 
     * 使用StringTokenizer类将字符串按分隔符转换成字符数组 
     * @param string 字符串  
     * @param separator 分隔符 
     * @return 字符串数组 
     * @see [类、类#方法、类#成员] 
     */  
    public static String[] split(String string, String separator)  
    {  
        int i = 0;  
        StringTokenizer tokenizer = new StringTokenizer(string, separator);  
          
        String[] str = new String[tokenizer.countTokens()];  
          
        while (tokenizer.hasMoreTokens())  
        {  
            str[i] = new String();  
            str[i] = tokenizer.nextToken();  
            i++;  
        }  
          
        return str;  
    }  
      
    /** 
     * 字符串解析，不使用StringTokenizer类和java.lang.String的split()方法 
     * 将字符串根据分割符转换成字符串数组 
     * @param string 字符串 
     * @return 解析后的字符串数组
     */  
    public static String[] split(String string, char separator)  
    {  
        //字符串中分隔符的个数  
        int count = 0;  
          
        //如果不含分割符则返回字符本身  
        if (string.indexOf(separator) == -1)  
        {  
            return new String[]{string};  
        }  
          
        char[] cs = string.toCharArray();  
          
        //过滤掉第一个和最后一个是分隔符的情况  
        for (int i = 1; i < cs.length -1; i++)  
        {  
            if (cs[i] == separator)  
            {  
                count++; //得到分隔符的个数  
            }  
        }  
          
        String[] strArray = new String[count + 1];  
        int k = 0, j = 0;  
        String str = string;  
          
        //去掉第一个字符是分隔符的情况  
        if ((k = str.indexOf(separator)) == 0)  
        {  
            str = string.substring(k + 1);  
        }  
          
        //检测是否包含分割符，如果不含则返回字符串  
        if (str.indexOf(separator) == -1)  
        {  
            return new String[]{str};  
        }  
          
        while ((k = str.indexOf(separator)) != -1)  
        {  
            strArray[j++] = str.substring(0, k);  
            str = str.substring(k + 1);  
            if ((k = str.indexOf(separator)) == -1 && str.length() > 0)  
            {  
                strArray[j++] = str.substring(0);  
            }  
        }  
          
        return strArray;  
    }

	/**
	 * 将一个 JavaBean 对象转化为一个  Map
	 *
	 * @param bean 要转化的JavaBean 对象
	 * @return 转化出来的  Map 对象
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	public static Map<String, Object> convertBean(Object bean) {
		Class type = bean.getClass();
		Map<String, Object> returnMap = new HashMap();
		try {
			BeanInfo beanInfo = null;
			beanInfo = Introspector.getBeanInfo(type);
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor descriptor : propertyDescriptors) {
				String propertyName = descriptor.getName();
				if (!propertyName.equals("class")) {
					Method readMethod = descriptor.getReadMethod();
					Object result = readMethod.invoke(bean, new Object[0]);
					if (result != null) {
						returnMap.put(propertyName, result);
					} else {
						returnMap.put(propertyName, "");
					}
				}
			}
		} catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
//                 * @throws IntrospectionException 如果分析类属性失败
//                 * @throws IllegalAccessException 如果实例化 JavaBean 失败
//                 * @throws InvocationTargetException 如果调用属性的 setter 方法失败
			e.printStackTrace();
		}
		return returnMap;
	}

	/**
	 * 将object转换为string
	 *
	 * @param obj object
	 * @return String
	 */
	public static String getString(Object obj) {
		if (obj != null) {
			return obj.toString();
		}
		return "";
	}

	/**
	 * 将N个string拼接成一个string
	 *
	 * @param str 字符串
	 * @return 字符串
	 */
	public static String toString(String... str) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (str != null && str.length > 0) {
			for (int i = 0; i < str.length; ) {
				stringBuilder.append(str[i++]);
			}
		}
		return stringBuilder.toString();
	}
}
