package hello;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.MessageDigest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.DigestScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

@RestController
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "Welcome to the home page!";
    }
    

    @RequestMapping("/signal")
    public String getValue(@RequestParam(value="name", defaultValue="World") String name) throws ClientProtocolException, IOException {
    	URL url = new URL("http://localhost");
//    	HttpGet request = new HttpGet("http://192.168.125.1/rw/iosystem/signals/EtherNetIP/FESTO_Terminal/DO2_Festo?json=1");
    	HttpHost targetHost = new HttpHost(url.getHost(),url.getPort(),url.getProtocol());
    	CloseableHttpClient client = HttpClients.createDefault();
    	HttpClientContext context = HttpClientContext.create();
    	
    	String username = "";
    	String password = "";
    	
    	/*encode nonce*/
//    	String nonce = null;
//    	MessageDigest sha1;
//    	try{
//    		sha1 = MessageDigest.getInstance("SHA-1");
//    		sha1.update(Base64.decodeBase64(robotService.genNonce()));
//    		nonce = new String(Base64.encodeBase64(sha1.digest()));
//    		sha1.reset();
//    	}catch(Exception e){
//    		e.printStackTrace();
//    	}
    	/*end*/
    	CredentialsProvider credPro = new BasicCredentialsProvider();
    	credPro.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username,password));
    	AuthCache authCache = new BasicAuthCache();
    	DigestScheme digestScheme = new DigestScheme();
    	digestScheme.overrideParamter("realm", "");
    	digestScheme.overrideParamter("algorithm","");
    	//digestScheme.overrideParamter("nonce", new String(Base64.encodeBase64(Long.toString(new Random().nextLong(), 92).getBytes())));
    	authCache.put(targetHost, digestScheme);
    	
    	context.setCredentialsProvider(credPro);
    	context.setAuthCache(authCache);
    	HttpGet httpGet = new HttpGet(url.getPath());
    	CloseableHttpResponse res = client.execute(targetHost, httpGet,context);
    	StringBuffer result = new StringBuffer();
    	
    	
    	try {
        	BufferedReader rd = new BufferedReader(new InputStreamReader(res.getEntity().getContent()));
        	String line = "";
        	while ((line = rd.readLine()) != null) {
        		result.append(line);
        	}
		} catch (Exception e) {
			// TODO: handle exception
		} finally{
			res.close();
		}
    	return result.toString();
    }
}
