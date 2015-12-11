package utils;

import java.io.*;

public class Serializer {
    public static byte[] serialize(Serializable elem) throws IOException{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();        
        ObjectOutputStream out = new ObjectOutputStream(baos);
        out.writeObject(elem);
        out.flush();
        return baos.toByteArray();
    }
    public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException{
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ObjectInputStream in = new ObjectInputStream(bais);
        return in.readObject();
    }
}
