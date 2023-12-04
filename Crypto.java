import com.google.gson.Gson;

import java.io.*;

public class Crypto implements Serializable {
    private String symbol,bidPrice,bidQty,askPrice,askQty;

    public Crypto(String symbol, String bidPrice, String bidQty, String askPrice, String askQty) {
        this.symbol = symbol;
        this.bidPrice = bidPrice;
        this.bidQty = bidQty;
        this.askPrice = askPrice;
        this.askQty = askQty;
    }

    static Crypto deserialize(String path){
        Gson gson=new Gson();
        StringBuilder str= new StringBuilder();
        try {
            BufferedReader in=new BufferedReader(new FileReader(path));
            String str2=in.readLine();
            while (str2!=null){
                str.append(str2);
                str2=in.readLine();
            }
        }catch (Exception e){

        }
        return gson.fromJson(str.toString(),Crypto.class);
    }
    void serialize(String path) throws IOException {
        Gson gson = new Gson();
        String str=gson.toJson(this);


        try (
                BufferedWriter out=new BufferedWriter(new FileWriter(path))) {
            out.write(str);
        }
    }

    public String getAskPrice() {
        return askPrice;
    }

    public String getBidPrice() {
        return bidPrice;
    }

    @Override
    public String toString() {
        return "Crypto{" +
                "symbol='" + symbol + '\'' +
                ", bidPrice=" + bidPrice +
                ", bidQty=" + bidQty +
                ", askPrice=" + askPrice +
                ", askQty=" + askQty +
                '}';
    }

    public String getSymbol() {
        return symbol;
    }
}
