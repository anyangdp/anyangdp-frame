package com.anyangdp.utils;

import com.anyangdp.handler.GenericResponse;
import org.springframework.http.*;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.util.StringUtils;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * @title：使用spring的restTemplate替代httpclient工具
 * @author：liuxing
 * @date：2015-05-18 08:48
 */
public class RestClient {
    private static RestTemplate restTemplate;

    static {
       /* // 长连接保持30秒
        PoolingHttpClientConnectionManager pollingConnectionManager = new PoolingHttpClientConnectionManager(30, TimeUnit.SECONDS);
        // 总连接数
        pollingConnectionManager.setMaxTotal(500);
        // 同路由的并发数
        pollingConnectionManager.setDefaultMaxPerRoute(500);

        HttpClientBuilder httpClientBuilder = HttpClients.custom();
        httpClientBuilder.setConnectionManager(pollingConnectionManager);
        // 重试次数，默认是3次，没有开启
        httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(2, true));
        // 保持长连接配置，需要在头添加Keep-Alive
        httpClientBuilder.setKeepAliveStrategy(DefaultConnectionKeepAliveStrategy.INSTANCE);

        List<Header> headers = new ArrayList<>();
        headers.add(new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.16 Safari/537.36"));
        headers.add(new BasicHeader("Accept-Encoding", "gzip,deflate"));
        headers.add(new BasicHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6"));
        headers.add(new BasicHeader("Connection", "keep-alive"));

        httpClientBuilder.setDefaultHeaders(headers);

        HttpClient httpClient = httpClientBuilder.build();

        // httpClient连接配置，底层是配置RequestConfig
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        // 连接超时
        clientHttpRequestFactory.setConnectTimeout(5000);
        // 数据读取超时时间，即SocketTimeout
        clientHttpRequestFactory.setReadTimeout(5000);
        // 连接不够用的等待时间，不宜过长，必须设置，比如连接不够用时，时间过长将是灾难性的
        clientHttpRequestFactory.setConnectionRequestTimeout(200);
        // 缓冲请求数据，默认值是true。通过POST或者PUT大量发送数据时，建议将此属性更改为false，以免耗尽内存。
        // clientHttpRequestFactory.setBufferRequestBody(false);
*/
        // 添加内容转换器
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        messageConverters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
        messageConverters.add(new FormHttpMessageConverter());
        messageConverters.add(new MappingJackson2XmlHttpMessageConverter());
        messageConverters.add(new MappingJackson2HttpMessageConverter());
        messageConverters.add(new ByteArrayHttpMessageConverter());

        restTemplate = new RestTemplate(messageConverters);
//        restTemplate.setRequestFactory(clientHttpRequestFactory);
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler());

//        LOGGER.info("RestClient初始化完成");
    }

    private RestClient() {}

    public static RestTemplate getClient() {
        return restTemplate;
    }
    
    /**
	 * 发送请求的并且返回数据的方法
	 * @param url 请求的url路径
	 * @param httpMethod  请求方式
	 * @param params 参数
	 * @param clazz
	 */
	public static <T> T requestDataForPojo(String url, HttpMethod httpMethod, Class<T> clazz, Object params) {
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Object> entity = new HttpEntity<Object>(params, header);
		try {
			ResponseEntity<String> re = restTemplate.exchange(url, httpMethod, entity,String.class);
			HttpStatus statusCode = re.getStatusCode();
			if(!HttpStatus.OK.equals(statusCode)) return null;

			String response = re.getBody();
			if (StringUtils.isEmpty(response)) return null;
			T objectValue;
			objectValue = JacksonUtil.fromJson(JacksonUtil.toJson(response), clazz);
			return objectValue;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static <T> T requestDataForPojoDTO(String url, HttpMethod httpMethod, Class<T> clazz, Object params) {
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Object> entity = new HttpEntity<Object>(params, header);
		try {
			ResponseEntity<GenericResponse> re = restTemplate.exchange(url, httpMethod, entity,GenericResponse.class);
			HttpStatus statusCode = re.getStatusCode();
			if(!HttpStatus.OK.equals(statusCode)) return null;

			GenericResponse response = re.getBody();
			if (StringUtils.isEmpty(response)) return null;
			T objectValue;
			objectValue = JacksonUtil.fromJson(JacksonUtil.toJson(response.getData()),clazz);
			return objectValue;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	public static <T> List<T> requestDataForPojoList(String url, HttpMethod httpMethod, Class<T> clazz, Object params) {
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Object> entity = new HttpEntity<>(params, header);
		try {
			ResponseEntity<GenericResponse> re = restTemplate.exchange(url, httpMethod, entity,GenericResponse.class);
			HttpStatus statusCode = re.getStatusCode();
			if(!HttpStatus.OK.equals(statusCode)) return null;
			GenericResponse response = re.getBody();
			if (StringUtils.isEmpty(response)) return null;
			List<T> t;
			t = JacksonUtil.json2List(JacksonUtil.toJson(response.getData()),clazz);
			return t;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}