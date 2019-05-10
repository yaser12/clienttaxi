package utility;

import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Helper class for working with a remote server
 */
public class HttpHelper {
    public static final String TAG = "HttpHelper";
    public static boolean weeak_connection=false;
    public static boolean connected_succesfully=false;
    public static boolean wait_so_longtime=false;
    /**
     * Returns text from a URL on a web server
     *
     * @param address
     * @return
     * @throws IOException
     */
    public static String downloadUrl(String address) throws IOException
    {

        InputStream is = null;
        try {
            connected_succesfully=false;
            weeak_connection=false;
            Log.i(TAG, "address: " +address);
            URL url = new URL(address);
            Log.i(TAG, "1 : " );
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            Log.i(TAG, "2 : " );
            conn.setReadTimeout(20000);
            Log.i(TAG, "3 : " );
            conn.setConnectTimeout(25000);
            Log.i(TAG, "4 : " );
            conn.setRequestMethod("GET");
            Log.i(TAG, "5 : " );
            conn.setDoInput(true);
            Log.i(TAG, "6 : " );
            conn.connect();
            connected_succesfully=true;
            Log.i(TAG, "7 : connected successfully " );

            int responseCode = conn.getResponseCode();
            Log.i(TAG, "readedStream: responseCode  ="+responseCode );
            if (responseCode != 200) {
                Log.i(TAG, "readedStream: responseCode != 200 ="+responseCode );
                throw new IOException("Got response code " + responseCode);
            }
            is = conn.getInputStream();
            String readedStream= readStream(is);
            Log.i(TAG, "readedStream: " +readedStream);
            return readedStream;

        }
        catch (java.net.SocketTimeoutException es) {
            Log.i(TAG, "IOException: " +es.toString());
          if(connected_succesfully)  weeak_connection=true;
            es.printStackTrace();
            return null;

        }
        catch (IOException e) {
            Log.i(TAG, "IOException: " +e.toString());
            e.printStackTrace();
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return null;
    }

    /**
     * Reads an InputStream and converts it to a String.
     *
     * @param stream
     * @return
     * @throws IOException
     */
    private static String readStream(InputStream stream) throws IOException {
        Log.i(TAG, "start readedStream: " );
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        BufferedOutputStream out = null;
        try {
            int length = 0;
            out = new BufferedOutputStream(byteArray);
            while ((length = stream.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            out.flush();
            return byteArray.toString();
        } catch (IOException e) {
            Log.i(TAG, "IOException : "+e.toString() );
            e.printStackTrace();
            return null;
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

}
