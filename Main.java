import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        ArrayList<Crypto> market=deserialize("crypto.json");//arraylist conente gli scambi
        Graph graph=new Graph();
        Set<String> nomiCrypto=gestioneDelleCrypto(market);

        for(Crypto c:market){
            String[] crypto=slash(c.getSymbol(),nomiCrypto);
            graph.addEdge(crypto[0],crypto[1], 1/*Double.parseDouble(c.getBidPrice())*/,false);
            graph.addEdge(crypto[1],crypto[0], 1/*Double.parseDouble(c.getAskPrice())*/,false);
        }
        graph.serialize("graph.json");
    }
    //legge il file json per avere la collezione di scambi
    static ArrayList<Crypto> deserialize(String path) throws IOException {
        Gson gson = new Gson();
        StringBuilder str = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(new FileReader(path));
            String str2 = in.readLine();
            while (str2 != null) {
                str.append(str2);
                str2 = in.readLine();
            }
        } catch (IOException e) {
            System.out.println("errore");
        }

        Type cryptoListType = new TypeToken<ArrayList<Crypto>>() {}.getType();
        return gson.fromJson(str.toString(), cryptoListType);
    }

    static Set<String> gestioneDelleCrypto(ArrayList<Crypto> market) throws IOException {
        BufferedWriter out=new BufferedWriter(new FileWriter("nomiCrypto.txt"));

        Set<String> nomi=new HashSet<>();
        //mette i nomi degli scambi nel file per poi cancellarli
        ArrayList<Crypto> scambi = new ArrayList<>(market);

        //mette crypto conosciute
        for(Crypto c:market){
            if(c.getSymbol().length()==6 &&( (c.getSymbol().contains("BTC")) || c.getSymbol().contains("ETH"))){
                nomi.add(c.getSymbol().substring(0,c.getSymbol().length()/2));
                nomi.add(c.getSymbol().substring(c.getSymbol().length()/2,c.getSymbol().length()));
                scambi.remove(c);
            }
        }
        int si=scambi.size(),prec=si;
        boolean b=false;
        while (!scambi.isEmpty()){
            Iterator<Crypto> iterator = scambi.iterator();

            while (iterator.hasNext()) {
                Crypto c = iterator.next();
                String nome = c.getSymbol();

                for (String str : nomi) {
                    String crypto = nome.replaceFirst(str, "");
                    if (!crypto.equals(nome) && !str.equals("T") &&  (nome.endsWith(str) || nome.startsWith(str) && b) ){
                        if(nome.contains("BIDR"))nomi.add(crypto);
                        iterator.remove();
                        si--;
                        nomi.add(crypto);

                        break;
                    }
                }
            }
            if(si==prec)b=true;
            prec=si;
        }
        for(String nomsi:nomi){
            out.write(nomsi);
            out.newLine();
            out.flush();
        }
            out.close();
            return nomi;

    }

    static String[] slash(String str, Set<String> nomiCrypto) {
        for (String crypto : nomiCrypto) {
            String s = str.replaceFirst(crypto, "");
            if (nomiCrypto.contains(s)) {
                return new String[]{s, crypto};
            }
        }

        return new String[]{"",""};
    }
}