package naver;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class NaverBoxoffice {
	
	public static void main(String[] args) throws IOException {
		
		String url="https://movie.naver.com/movie/running/current.nhn";
		
		Document doc=Jsoup.connect(url).get();
		
		Elements movieList=doc.select("div.lst_wrap>ul.lst_detail_t1 li");
		
		String title=""; //영화 제목
		String score=""; //평점
		String reserve=""; //예매율
		String genre=""; //장르
		String runningTime=""; //상영 시간
		String openDate=""; //개봉일
		String director=""; //감독
		String actor=""; //출연진
		String naverCode=""; //네이버 영화 코드
		
		for(Element movie:movieList) {
			
			reserve="0.0"; //예매율 기본값으로 설정해, 예매율이 존재하는 영화만 새로운 값이 담기도록 함
			director="";
			actor="";
			
			title=movie.select("dt.tit>a").text();
			
			//String reserve=movie.select("span.num").get(1).text();
			//예매율이 존재하지 않는 항목이 존재할 수 있기 때문에(N/A) 조건문을 사용한다
			if (movie.select("span.num").size()==2) {
				reserve=movie.select("span.num").get(1).text();
			}
			
			score=movie.select("span.num").get(0).text();
			
			genre=movie.select("dd>span.link_txt").get(0).text();
			
			//한 덩어리로 묶인 상영시간과 개봉일을 낱개로 꺼내기
			//상영시간
			String temp=movie.select("dl.info_txt1>dd").get(0).text();
			int beginTimeIndex=temp.indexOf("|"); //앞에서부터 인덱스 번호 계산
			int endTimeIndex=temp.lastIndexOf("|"); //뒤에서부터 인덱스 번호 계산
			
			if (beginTimeIndex==endTimeIndex) { //상영시간
				runningTime=temp.substring(0, endTimeIndex); //장르가 설정되지 않아 앞으로 한 칸씩 당겨진 경우
			} else {
				runningTime=temp.substring(beginTimeIndex+2, endTimeIndex); //장르가 설정되어 제대로 입력된 경우
			}
			
			//0 : 없음, 1 : 있음
			int diCode=0; //감독 유무 확인
			int acCode=0; //출연진 유무 확인
			
			/*
			 * equals() : 동등비교(객체자료형 String)
			 * 객체자료형은 변수에 저장한 값의 주소가 들어있기 때문에 ==을 사용할 수 없다.
			 * equals는 객체자료형 변수의 실제 값을 가져와 서로 비교한다.
			 * ==를 사용해도 오류가 발생하진 않지만 결과값으로 False를 출력한다.
			 * == : 동등비교(기본자료형)
			 */
			if (!movie.select("dt.tit_t2").text().equals("")) {
				diCode=1; //감독이 있는 경우
			}
			
			if (!movie.select("dt.tit_t3").text().equals("")) {
				acCode=1; //출연진이 있는 경우
			}
			
			//감독이나 출연진이 존재하지 않는 각각의 경우
			if (diCode==1&&acCode==0) {
				director=movie.select("dd>span.link_txt").get(1).text(); //감독
			}	else if(diCode==0&&acCode==1) {
					actor=movie.select("dd>span.link_txt").get(1).text(); //출연진
			}	else if(diCode==1&&acCode==1) {
					director=movie.select("dd>span.link_txt").get(1).text(); //감독
					actor=movie.select("dd>span.link_txt").get(2).text(); //출연진
			}
			
			String naverHref=movie.select("dt.tit>a").attr("href"); //네이버 영화 URL
			naverCode=naverHref.substring(naverHref.lastIndexOf("=")+1); //네이버 영화 코드
			
			//개봉일
			int openDtTextIndex=temp.lastIndexOf("개봉"); //개봉이라는 글자를 제외하기 위한 변수
			openDate=temp.substring(endTimeIndex+2, openDtTextIndex);
						
			System.out.println("제목 : "+title);
			System.out.println("평점 : "+score+"점");
			System.out.println("예매율 : "+reserve+"%");
			System.out.println("장르 : "+genre);
			System.out.println("상영시간 : "+runningTime);
			System.out.println("개봉일 : "+openDate);
			System.out.println("감독 : "+director);
			System.out.println("출연진 : "+actor);
			System.out.println("영화코드 : "+naverCode);
			System.out.println();

			}
		
	}

}
