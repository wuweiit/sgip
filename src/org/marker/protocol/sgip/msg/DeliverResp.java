package org.marker.protocol.sgip.msg;

 

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.marker.protocol.tools.Utils;

 
public class DeliverResp extends Message {

    int result;
    String reserve;

    public DeliverResp()
    {
        super(0x80000004);
        getHead().setLength(getLength() + 9);
    }

    public DeliverResp(MsgHead head)
    {
        super(0x80000004);
        getHead().setLength(getLength() + 9);
        getHead().setSequence1(head.getSequence1());
        getHead().setSequence2(head.getSequence2());
        getHead().setSequence3(head.getSequence3());
    }

    public String getReserve()
    {
        return reserve;
    }

    public void setReserve(String reserve)
    {
        this.reserve = reserve;
    }

    public int getResult()
    {
        return result;
    }

    public void setResult(int result)
    {
        this.result = result;
    }

    public void write(OutputStream out)
        throws IOException
    {
        getHead().write(out);
        out.write(result);
        byte bs[] = new byte[8];
        if(reserve != null)
            System.arraycopy(reserve.getBytes(), 0, bs, 0, Math.min(reserve.getBytes().length, 8));
        out.write(bs);
        out.flush();
    }

    public void read(InputStream in)
        throws IOException
    {
        byte bs[] = new byte[9];
        int rc = in.read(bs);
        if(rc < 9)
        {
            throw new EOFException(String.valueOf(rc));
        } else
        {
            result = bs[0];
            reserve = Utils.getString(bs, 1, 8);
            return;
        }
    }
}
