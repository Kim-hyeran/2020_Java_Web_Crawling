package daum;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DaumMovie {
	
	public static void main(String[] args) throws IOException {
		
		int page=1;
		int count=0;
		
		String writer="";
		String contents="";
		int score=0;
		String regDate="";
		
		while(true) {
			String url="https://movie.daum.net/moviedb/grade?movieId=134684&type=netizen&page="+page;
			
			Document doc=Jsoup.connect(url).get();
			
			Elements replyList=doc.select("div.main_detail li");
			
			//네이버 영화 페이지와는 달리 빈 페이지를 출력하기 때문에 조건문이 달라진다.
		    if(replyList.size()==0) {
		        break;
		    }
			
			for(Element reply:replyList) {
				
				writer=reply.select("div.review_info>a>em").text();
				contents=reply.select("p.desc_review").text();
				score=Integer.parseInt(reply.select("div.raking_grade>em").text());
				String preRegDate=reply.select("span.info_append").text();
				regDate=preRegDate.substring(0, preRegDate.lastIndexOf(","));
				
				System.out.println("작성자 : "+writer);
				System.out.println("리뷰 : "+contents);
				System.out.println("평점 : "+score);
				System.out.println("날짜 : "+regDate);
				System.out.println();
				
				count+=1;
				
			}
		
		page+=1;
		
		}
		
		System.out.println(count+"건의 영화 리뷰를 수집하였습니다.");
		
	}

}
