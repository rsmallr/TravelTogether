package com.officialwebsite.traveltogether;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.officialwebsite.traveltogether.Data.ICultureOpenData;
import com.officialwebsite.traveltogether.Data.TaipeiOpenData;

public class OpenDataHelper{
	private static final String TAG = OpenDataHelper.class.getName();
	Context mContext = null;
	
	public void init(final Activity context){
		mContext = context;
		Thread parseTask = new Thread(){
			
			public void run(){
				context.runOnUiThread(new Runnable(){

					@Override
					public void run() {
						Toast.makeText(context, "Browse Start!", Toast.LENGTH_LONG).show();
					}
					
				});
				parseTaipei();
				
//				parseICulture(1);
				parseICulture(2);
//				parseICulture(3);
//				parseICulture(4);
//				parseICulture(5);
//				parseICulture(6);
//				parseICulture(7);
//				parseICulture(8);
//				parseICulture(11);
//				parseICulture(13);
//				parseICulture(14);
//				parseICulture(15);
//				parseICulture(16);
//				parseICulture(17);
				
				context.runOnUiThread(new Runnable(){

					@Override
					public void run() {
						Toast.makeText(context, "Browse Done!", Toast.LENGTH_LONG).show();
					}
					
				});
				
//				ArrayList<TaipeiOpenData> yt1 = OpenDataDbAdapter.getInstance(mContext).searchTaipeiOpenDataByKeyword("Wanhua Dist");
//				Log.d(TAG,"aggggg="+yt1.get(0).address);
//				
//				ArrayList<ICultureOpenData> yt = OpenDataDbAdapter.getInstance(mContext).searchICultureOpenDataWithCategoryIdAndLocation("1", "�啣�");
//				Log.d(TAG,"aggggg="+yt.get(0).location);
			}
		};
		
		parseTask.start();
	}
	
	private void parseTaipei(){
		try{
			Log.d(TAG,"parseTaipei");
			HttpClient client = new DefaultHttpClient();
			//HttpUriRequest request = new HttpGet("http://data.taipei.gov.tw/opendata/apply/query/Q0QzRjMyOEItRjlGMi00NDMyLUI1OTMtNTkyM0YzMzhEM0RB?$format=xml&$filter=");
			HttpUriRequest request = new HttpGet("http://data.taipei.gov.tw/opendata/apply/query/MzVERDUyOTItNjI1NC00NjcyLUE3OEItNDY3ODhDMURFM0Yy?$format=xml&$filter=");
			HttpResponse response = client.execute(request);
	        HttpEntity r_entity = response.getEntity();
	        String xmlString =  new String(EntityUtils.toString(r_entity).getBytes("ISO-8859-1"), "UTF-8");
	        //String xmlString = EntityUtils.toString(r_entity);
	        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder db = factory.newDocumentBuilder();
	        InputSource inStream = new InputSource();
	        inStream.setCharacterStream(new StringReader(xmlString));
	        Document doc = db.parse(inStream);  
	
	        Log.d(TAG,"RootElement :" + doc.getDocumentElement().getNodeName());
	        NodeList nList = doc.getElementsByTagName("row");

			for (int i = 0; i < nList.getLength(); i++) {

				Node iNode = nList.item(i);

				Log.d(TAG,"CurrentElement :" + iNode.getNodeName());
				if (iNode.getNodeType() == Node.ELEMENT_NODE) {
					TaipeiOpenData data = new TaipeiOpenData();
					Element eElement = (Element) iNode;
					//eElement.getAttribute("UID")
					data.stitle = getTextByTag(eElement,"stitle");
					data.xbody = getTextByTag(eElement,"xbody");
					data.info = getTextByTag(eElement,"info");
					data.address = getTextByTag(eElement,"address");
					
					NodeList fileList = eElement.getElementsByTagName("file");
					Node fileNode = fileList.item(0);
					Log.i(TAG,"fileList count="+fileList.getLength());
					if(fileNode != null){
						Element fileElement = (Element) fileNode;
						
						String fileText = fileElement.getTextContent();
						String[] AfterSplit = fileText.split("[><]+");
//						for(int j=0;j<AfterSplit.length;++j){
//							Log.i(TAG,"fileText="+AfterSplit[j]+" j="+j);
//						}
						String imageUrl = "";
						if(AfterSplit.length > 3){
							Log.i(TAG,"fileElement="+AfterSplit[3]);
							imageUrl = AfterSplit[3];
						}
						data.imageUrl = imageUrl;
					}
					
					Log.i(TAG,"stitle="+data.stitle);
					Log.i(TAG,"xbody="+data.xbody);
					Log.i(TAG,"info="+data.info);
					Log.i(TAG,"address="+data.address);
					
					OpenDataDbAdapter.getInstance(mContext).addTaipeiOpenData(data);
				}
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			
		}
	}
	
	public static String toUtf8(String str) {
		try {
			return new String(str.getBytes("BIG5"),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
	
	private void parseICulture(int category){
		try{
			HttpClient client = new DefaultHttpClient();
			HttpUriRequest request = new HttpGet("http://cloud.culture.tw/frontsite/trans/SearchShowAction.do?method=doFindTypeX&category="+category);
			HttpResponse response = client.execute(request);
	        HttpEntity r_entity = response.getEntity();
	        String xmlString = EntityUtils.toString(r_entity);
	        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder db = factory.newDocumentBuilder();
	        InputSource inStream = new InputSource();
	        inStream.setCharacterStream(new StringReader(xmlString));
	        Document doc = db.parse(inStream);  
	
	        Log.d(TAG,"RootElement :" + doc.getDocumentElement().getNodeName());
	        NodeList nList = doc.getElementsByTagName("Info");

			for (int i = 0; i < nList.getLength(); i++) {

				Node iNode = nList.item(i);

				Log.d(TAG,"CurrentElement :" + iNode.getNodeName());
				if (iNode.getNodeType() == Node.ELEMENT_NODE) {

					ICultureOpenData data = new ICultureOpenData();
					
					Element eElement = (Element) iNode;
					//eElement.getAttribute("UID")
					String UID = eElement.getAttribute("UID");
					data.stitle = eElement.getAttribute("title");//
					data.category = String.valueOf(category);//eElement.getAttribute("category");//
					String showUnit = eElement.getAttribute("showUnit");
					data.xbody = eElement.getAttribute("descriptionFilterHtml");
					String discountInfo = eElement.getAttribute("discountInfo");
					data.imageUrl = eElement.getAttribute("imageUrl");//thumbnail
					String masterUnit = eElement.getAttribute("masterUnit");
					String subUnit = eElement.getAttribute("subUnit");
					String supportUnit = eElement.getAttribute("supportUnit");
					String otherUnit = eElement.getAttribute("otherUnit");
					String webSales = eElement.getAttribute("webSales");
					String sourceWebPromote = eElement.getAttribute("sourceWebPromote");
					String comment = eElement.getAttribute("comment");
					String editModifyDate = eElement.getAttribute("editModifyDate");
					String sourceWebName = eElement.getAttribute("sourceWebName");
					data.startDate = eElement.getAttribute("startDate");
					data.endDate = eElement.getAttribute("endDate");
					
					Log.i(TAG,"UID="+UID);
					Log.i(TAG,"title="+data.stitle);
					Log.i(TAG,"category="+data.category);
					Log.i(TAG,"showUnit="+showUnit);
					Log.i(TAG,"descriptionFilterHtml="+data.xbody);
					Log.i(TAG,"discountInfo="+discountInfo);
					Log.i(TAG,"imageUrl="+data.imageUrl);
					Log.i(TAG,"masterUnit="+masterUnit);
					Log.i(TAG,"subUnit="+subUnit);
					Log.i(TAG,"supportUnit="+supportUnit);
					Log.i(TAG,"otherUnit="+otherUnit);
					Log.i(TAG,"webSales="+webSales);
					Log.i(TAG,"sourceWebPromote="+sourceWebPromote);
					Log.i(TAG,"editModifyDate="+editModifyDate);
					Log.i(TAG,"sourceWebName="+sourceWebName);
					Log.i(TAG,"startDate="+data.startDate);
					Log.i(TAG,"endDate="+data.endDate);
					NodeList nodeList = eElement.getElementsByTagName("element");
					
					if(nodeList == null){
						Log.e(TAG,"invalid tag!");
					}
					
					for(int j=0;j<(nodeList.getLength()>0?1:0);j++){
						Node jnode = nodeList.item(j);
						Log.i(TAG,"nodeListnodeList="+jnode.getNodeName());
						if (jnode.getNodeType() == Node.ELEMENT_NODE) {

							Element jElement = (Element) jnode;
							 
							String time = jElement.getAttribute("time");
							data.address = jElement.getAttribute("location");
							data.locationName = jElement.getAttribute("locationName");
							String onsales = jElement.getAttribute("onsales");
							data.latitude = jElement.getAttribute("latitude");
							data.longitude = jElement.getAttribute("longitude");
							String price = jElement.getAttribute("price");
							
							Log.i(TAG,"time="+time);
							Log.i(TAG,"location="+data.address);
							Log.i(TAG,"locationName="+data.locationName);
							Log.i(TAG,"onsales="+onsales);
							Log.i(TAG,"latitude="+data.latitude);
							Log.i(TAG,"longitude="+data.longitude);
							Log.i(TAG,"price="+price);
							
							break;
							
						}
					}
					OpenDataDbAdapter.getInstance(mContext).addICultureData(data);
				}
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			
		}
	}
	private static String getTextByTag(Element element, String tag){
		String text = "";
		NodeList nodeList = element.getElementsByTagName(tag);
		if(nodeList == null){
			Log.e(TAG,"invalid tag!");
			return null;
		}
		Node node = nodeList.item(0);
		if(node == null){
			Log.e(TAG,"invalid index!");
			return null;
		}
		text = node.getTextContent();
		Log.d(TAG,"getTextByTag : "+tag+"=" + text);
		return text;
	}
	
}
