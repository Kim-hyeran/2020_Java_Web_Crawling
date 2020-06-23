package naver;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

//평점 목록 전체 페이지를 돌면서 리뷰 내용, 평점, 작성자, 작성일자 수집
public class NaverMovie {
	
	public static void main(String[] args) throws IOException {
		
		int page=1;
		int count=0;
		
		String writer="";
		String contents="";
		int score=0; //DB에서 계산 처리를 거치게 설정할 수 있음
		String regDate="";
		String prePage="";
				
		while(true) {
			String url="https://movie.naver.com/movie/bi/mi/pointWriteFormList.nhn?code=189633&type=after&isActualPointWriteExecute=false&isMileageSubscriptionAlready=false&isMileageSubscriptionReject=false&page="+page;
			
			Document doc=Jsoup.connect(url).get();
			
			Elements replyList=doc.select("div.score_result li");
			String nowPage=doc.select("input#page").attr("value");
			
			//리뷰 마지막 페이지에 도달하면 반복문을 중단하게 만드는 코드
			if(nowPage.equals(prePage)) {
				break;
			} else {
				prePage=nowPage;
			}
			
			for(Element reply:replyList) {
				
				writer=reply.select("div.score_reple a>span").text();
				contents=reply.select("div.score_reple>p>span").text();
				score=Integer.parseInt(reply.select("div.star_score>em").text()); //String형으로 가져온 데이터를 integer형으로 형변환
				String preRegDate=reply.select("div.score_reple em").get(1).text();
				regDate=preRegDate.substring(0, preRegDate.lastIndexOf(" "));
				
				System.out.println("작성자 : "+writer);
				System.out.println("리뷰 : "+contents);
				System.out.println("평점 : "+score);
				System.out.println("날짜 : "+regDate);
				System.out.println();
				
				count+=1; //or count++;
				
			}
			
			page+=1; //or page++;
		
		}
		
		System.out.println(count+"건의 영화 리뷰를 수집하였습니다.");
		
	}

}
