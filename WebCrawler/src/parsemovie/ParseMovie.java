package parsemovie;

import java.io.BufferedInputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ParseMovie {
	
	String key="e8ad51d844240caced9de66d57fa9f36";
	String today="";
	String[][] arrBoxOffice=new String[10][4];
	
	//1. 파싱하고 싶은 URL 생성(일일 박스오피스 어제자)
	// 	 URL=기본URL+key+날짜
	public String makeURL() {
		
		//어제 날짜 구하기(오늘 날짜 : date import)
		Calendar cal=Calendar.getInstance();
		cal.add(Calendar.DATE, -1); //날짜를 하루 전으로 설정
		System.out.println("포맷 전 : "+cal.getTime());
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd"); //연도 4자리+월 두 자리+날짜 두 자리
		today=sdf.format(cal.getTime());
		System.out.println("포맷 후 : "+today);
		
		String url="http://www.kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/"+"searchDailyBoxOfficeList.json"+"?key="+key+"&targetDt="+today;
		return url;
		
	}
	
	private String readUrl(String preUrl) throws Exception {
		
		//buffer : 임시 저장 공간
		BufferedInputStream reader=null; //전송된 데이터의 크키가 클 경우, 데이터를 쪼개 전송하는데 stream 코드를 사용해 일괄적으로 불러옴
		//불러온 데이터를 reader에 담기
		
		try {
			URL url=new URL(preUrl); //네트워크 상의 데이터기 때문에 URL을 사용해 데이터를 가져와야 한다
			reader=new BufferedInputStream(url.openStream());
			StringBuffer buffer=new StringBuffer();
			int i;
			byte[] b=new byte[4096]; //4096byte 크기로 데이터를 쪼개도록 지시하는 코드
			//임의의 크기를 지정해도 되지만 byte의 경우 4096byte로 설정하는 것이 일반적이다
			while((i=reader.read(b))!=-1) { //b의 크키만큼 쪼갠 데이터를 i에 저장, -1은 데이터가 더 이상 존재하지 않을 때 출력하는 값
				buffer.append(new String(b, 0, i)); //buffer의 마지막 저장 데이터 뒤에 계속해서 데이터 저장
			}
			return buffer.toString(); //buffer에 저장된 데이터를 문자열로 변경
		} finally {
			if(reader!=null) {
				reader.close();
			}
			
		}
	}
	
	public String[][] getBoxOffice(String url) throws Exception {
		
		JSONParser parser=new JSONParser();
		
		JSONObject obj=(JSONObject)parser.parse(readUrl(url)); //가져온 데이터를 JSON type으로 담기
		JSONObject json=(JSONObject)obj.get("boxOfficeResult"); //boxOfficeResult : 전체 데이터를 총괄(key), 전체를 가져와 json에 담기
		//이 경우 value는 영화 박스오피스 데이터 전체(1위부터 10위까지)가 된다.
		JSONArray array=(JSONArray)json.get("dailyBoxOfficeList"); //각각의 영화(dailyBoxOfficeList)로 쪼개어 array에 담기
		//dailyBoxOfficeList(key)의 value 값은 영화 각각의 제목, 순위, 누적관객수 등의 데이터가 된다.
		
		for(int i=0;i<array.size();i++) {
			
			JSONObject entity=(JSONObject)array.get(i);
			
			String rank=(String)entity.get("rank"); //순위
			String movieNm=(String)entity.get("movieNm"); //영화제목
			String audiAcc=(String)entity.get("audiAcc"); //누적관객수
			String salesAcc=(String)entity.get("salesAcc"); //누적매출액
			
			arrBoxOffice[i][0]=rank;
			arrBoxOffice[i][1]=movieNm;
			arrBoxOffice[i][2]=audiAcc;
			arrBoxOffice[i][3]=salesAcc;
		}
		
		return arrBoxOffice;
		
	}

}
