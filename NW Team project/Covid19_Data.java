import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class Covid19_Data {
	/* 공공데이터 */
	// 확진자수 
	public String today_decidecnt;    
	public String yesterday_decidecnt;
	public String increase_decidecnt;
	
	// 검사 진행 수
	public String today_examcnt;
	public String yesterday_examcnt;
	public String increase_examcnt;
	
	// 격리해제 수
	public String today_clearcnt;
	public String yesterday_clearcnt;
	public String increase_clearcnt;
	
	// 사망자 수 
	public String today_deathcnt;
	public String yesterday_deathcnt;
	public String increase_deathcnt;
	
    public Covid19_Data(Date start_day, Date end_day) throws Exception {
    	
    	SimpleDateFormat hm = new SimpleDateFormat ( "HHmm", Locale.KOREA );
    	SimpleDateFormat ymd = new SimpleDateFormat ( "yyyyMMdd", Locale.KOREA );
		Date currentTime = new Date();
		String mTime = hm.format(currentTime);
		if(Integer.parseInt(mTime) < 1000) // 업데이트가 아직 안된 경우
		{
			start_day = new Date(start_day.getTime()+(1000*60*60*24*-1));
			end_day = new Date(end_day.getTime()+(1000*60*60*24*-1));
		}
		String start = ymd.format(start_day);
		String end = ymd.format(end_day);;
        StringBuilder urlBuilder = new StringBuilder("http://openapi.data.go.kr/openapi/service/rest/Covid19/getCovid19InfStateJson"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=0iLwuYSOjR3un2V0T%2Fy2zbdTH2mIpi35ahCCViU7SNzKUhJUnIKxdZ2u1zlmgQSDyXQO0aPwSbcfWyQ4FamsMQ%3D%3D"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("startCreateDt","UTF-8") + "=" + URLEncoder.encode(start, "UTF-8")); /*검색할 생성일 범위의 시작*/
        urlBuilder.append("&" + URLEncoder.encode("endCreateDt","UTF-8") + "=" + URLEncoder.encode(end, "UTF-8")); /*검색할 생성일 범위의 종료*/
 
        Node today = null;
        Node yesterday = null;
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuffer sb = new StringBuffer();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();

       
        try {
        	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance(); 
        	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        	
        	FileOutputStream output = new FileOutputStream("./Covid19");
        	output.write(sb.toString().getBytes());
        	output.close();
        	
        	Document doc = dBuilder.parse("./Covid19");
        	doc.getDocumentElement().normalize();
        	
        	Element body = (Element)doc.getElementsByTagName("body").item(0);
        	Element items = (Element)body.getElementsByTagName("items").item(0);
        	Element today_item = (Element)items.getElementsByTagName("item").item(0);
        	Element yesterday_item = (Element)items.getElementsByTagName("item").item(1);
        	today = today_item.getElementsByTagName("decideCnt").item(0);
        	yesterday = yesterday_item.getElementsByTagName("decideCnt").item(0);
      
        	today_decidecnt = today.getTextContent();
        	yesterday_decidecnt = yesterday.getTextContent();
        	increase_decidecnt = Integer.toString(Integer.parseInt(today_decidecnt) -  Integer.parseInt(yesterday_decidecnt));
        	today = today_item.getElementsByTagName("examCnt").item(0);
        	yesterday = yesterday_item.getElementsByTagName("examCnt").item(0);
        	
        	today_examcnt = today.getTextContent();
        	yesterday_examcnt = yesterday.getTextContent();
        	increase_examcnt = Integer.toString(Integer.parseInt(today_examcnt) -  Integer.parseInt(yesterday_examcnt));
        	today = today_item.getElementsByTagName("clearCnt").item(0);
        	yesterday = yesterday_item.getElementsByTagName("clearCnt").item(0);
        	
        	today_clearcnt = today.getTextContent();
        	yesterday_clearcnt = yesterday.getTextContent();
        	increase_clearcnt = Integer.toString(Integer.parseInt(today_clearcnt) -  Integer.parseInt(yesterday_clearcnt));
        	today = today_item.getElementsByTagName("deathCnt").item(0);
        	yesterday = yesterday_item.getElementsByTagName("deathCnt").item(0);
        	
        	today_deathcnt = today.getTextContent();
        	yesterday_deathcnt = yesterday.getTextContent();
        	increase_deathcnt = Integer.toString(Integer.parseInt(today_deathcnt) -  Integer.parseInt(yesterday_deathcnt));
        
        	
        } catch(Exception e) {
        	e.printStackTrace();
        }
    }
    
//    public static void main(String[] args) {
//    	Covid19_Data cd = new Covid19_Data("20201120", "20201121");
//    }
}
