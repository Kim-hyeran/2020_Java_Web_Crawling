package naver;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element; 
import org.jsoup.select.Elements; //Elements : 복수, 여러 건

//java로 web crawling 하기

public class NaverFinance {
	
	//static : base를 사용하려는 곳이 일반 method면 static 생략이 가능하지만, static method에는 반드시 변수에 static을 첨부해야 변수를 사용할 수 있다.
	static String base="https://finance.naver.com/item/frgn.nhn?code=095700&page=1";
	
	//throws ~ : 예외 발생 시 직접 처리하지 않고, 자신을 호출한 곳으로 예외를 넘겨버림
	public static void main(String[] args) throws IOException {
		
		//connect : base 사이트로 가서 전체 페이지의 소스코드를 가져옴
					//python에서 requests와 같은 기능
		Document doc=Jsoup.connect(base).get();
		
		Elements line=doc.select("table.type2>tbody>tr");
		
		//향상된 for 반복문 : foreach 구문
		for(Element element:line) { //전체 데이터(복수)에서 한 건씩 꺼내 element(단수)에 담기
			
			Elements tds=element.select("td"); //tds : 단건으로 뽑아낸 tr의 안에 있는 td들
			
			if(tds.size()==9) { //거래량 표만 추출하기 위한 조건문
				String regdate=tds.get(0).text();
				System.out.println("날짜 : "+regdate);
				String price=tds.get(1).text();
				System.out.println("종가 : "+price);
				String yesterday=tds.get(2).text();
				System.out.println("전일비 : "+yesterday);
				String updown=tds.get(3).text();
				System.out.println("등락률 : "+updown);
				String transaction=tds.get(4).text();
				System.out.println("거래량 : "+transaction);
				System.out.println("기관 순매매량 : "+tds.get(5).text());
				System.out.println("외국인 순매매량 : "+tds.get(6).text());
				System.out.println("외국인 보유주 : "+tds.get(7).text());
				System.out.println("외국인 보유율 : "+tds.get(8).text());
				System.out.println();
			}
		
		}
		
	}

}
