import java.io.*;
import java.net.*;
import java.util.StringTokenizer;


class comunicacaoBrowser implements Runnable {
	 
	 Socket soc; 
	 OutputStream os; 
	 DataInputStream is;
	 String resource, pedido;
	 
	 //pega o pedido do browser
	 void recebePedido() {
		 try {
			 pedido ="";
			 boolean header = true;
			 String mensagem;
			 while ((mensagem = is.readLine()) != null) {
				 if (mensagem.equals(""))
					 break; //nesse caso não temos nada
				 
				 //para analisar 
				 StringTokenizer t = new StringTokenizer(mensagem);
				 String token = t.nextToken(); // pega a primeira parte
				 if (token.equals("GET")){ // se a primeira parte for "GET", então pega a segunda
					 resource = t.nextToken();
					 header = false;
				 }
				 if (header==false)
					 pedido += mensagem + "\n";
			 }
		 } catch (IOException e) {
			 System.err.println("Error receiving Web request");
			 e.printStackTrace();
		 }
	 }
	 
	 //retorna a resposta
	 void retornaResposta() {
		 String address="", request="/";
		 boolean emptyRequest = true;
		 try {
			 char[] res = resource.toCharArray();
			 for(int i=1;i<resource.length();i++){
				 if(emptyRequest){
					 if(res[i]=='/'){
						 emptyRequest=false;
					 }
					 else
						 address += res[i];
				 }
				 else{
					 request += res[i];
				 }
			 }
			 comunicacaoSite w = new comunicacaoSite(address, 80);
			 w.pedido(request,address);
			 byte[] write_b = w.receberRespostaString().getBytes();
			 os.write(write_b);
			 w.close();
		 } catch (IOException e) {
			 System.err.println("IOException in reading in Web " + "server");
			 e.printStackTrace();

		 }
	 }
	 
	 public void run() {
		 recebePedido();
		 retornaResposta();
		 close();
	 }
	 

	public static void main(String args[]) {
		 try {
			 ServerSocket s = new ServerSocket(8080);
			 for (;;) {
				 comunicacaoBrowser w = new comunicacaoBrowser(s.accept());
				 Thread thr = new Thread(w);
				 thr.start();

			 }
		 } catch (IOException i) {
			 System.err.println("IOException in Server");
			 i.printStackTrace();
		 }
	 }
	 
	 public void close() {
		 try {
			 is.close(); 
			 os.close(); 
			 soc.close();
		 } catch (IOException e) {
			 System.err.println("IOException in closing connection");
			 e.printStackTrace();
		 }
	 }
	 comunicacaoBrowser(Socket s) throws IOException {
		 soc = s;
		 os = soc.getOutputStream();
		 is = new DataInputStream(soc.getInputStream());
	 }
	 
	 
} 