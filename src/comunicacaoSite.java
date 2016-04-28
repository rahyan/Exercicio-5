import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

class comunicacaoSite {
	 Socket soc; 
	 OutputStream os; 
	 InputStream is;
	 
	 comunicacaoSite(String server, int port)	 throws IOException, UnknownHostException {
		 soc = new Socket(server, port);
		 os = soc.getOutputStream();
		 is = soc.getInputStream();
	 }
	 
	 void pedido(String path, String host) {
		PrintWriter outw = new PrintWriter(os, false);
		outw.print("GET " + path + " HTTP/1.1\r\n");
		outw.print("Host: " + host + ":8080\r\n");
		outw.print("Accept: */*\r\n Accept-Encoding: gzip, deflate\r\n User-Agent: runscope/0.1\r\n\r\n");
		outw.flush();
	 }

	 String receberRespostaString(){
		 int c;
		 String result = "";
		 System.out.println("GET RESPONSE STRING:");
		 try{
			 while ((c = is.read()) != -1){
				 System.out.print((char) c);
				 result += (char)c;
			 }
		 } catch (IOException e){
			 System.err.println("IOException in reading from " + "Web server");
		 }
		 return result;
	 }
	 
	 
	 public void close() {
		 try {
			 is.close(); os.close(); soc.close();
		 } catch (IOException e) {
			 System.err.println("IOException in closing connection");
		 }
	 }

}