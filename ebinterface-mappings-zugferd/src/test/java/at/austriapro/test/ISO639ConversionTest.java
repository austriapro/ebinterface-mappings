package at.austriapro.test;

import at.austriapro.utils.ISO639ConversionUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Paul on 10.09.2015.
 */
public class ISO639ConversionTest {
    @Test
    public void testISO639_2ToISO639_1(){
        String iSO639_2 = "GER";
        String iSO639_1 = ISO639ConversionUtil.convertISO639_2ToISO639_1(iSO639_2);
        Assert.assertEquals(iSO639_1, "DE");

        iSO639_2 = "BEL";
        iSO639_1 = ISO639ConversionUtil.convertISO639_2ToISO639_1(iSO639_2);
        Assert.assertEquals(iSO639_1, "BE");

        iSO639_2 = "XXX";
        iSO639_1 = ISO639ConversionUtil.convertISO639_2ToISO639_1(iSO639_2);
        Assert.assertNull(iSO639_1);
    }

    @Test
    public void testISO639_1ToISO639_2Bibliographic(){
        String iSO639_2 = "DE";
        String iSO639_1 = ISO639ConversionUtil.convertISO639_1ToISO639_2Bibliographic(iSO639_2);
        Assert.assertEquals(iSO639_1, "GER");

        iSO639_2 = "BE";
        iSO639_1 = ISO639ConversionUtil.convertISO639_1ToISO639_2Bibliographic(iSO639_2);
        Assert.assertEquals(iSO639_1, "BEL");

        iSO639_2 = "XX";
        iSO639_1 = ISO639ConversionUtil.convertISO639_1ToISO639_2Bibliographic(iSO639_2);
        Assert.assertNull(iSO639_1);
    }

    @Test
    public void testISO639_1ToISO639_2Terminology(){
        String iSO639_2 = "DE";
        String iSO639_1 = ISO639ConversionUtil.convertISO639_1ToISO639_2Terminology(iSO639_2);
        Assert.assertEquals(iSO639_1, "DEU");

        iSO639_2 = "BE";
        iSO639_1 = ISO639ConversionUtil.convertISO639_1ToISO639_2Terminology(iSO639_2);
        Assert.assertEquals(iSO639_1, "BEL");

        iSO639_2 = "XX";
        iSO639_1 = ISO639ConversionUtil.convertISO639_1ToISO639_2Terminology(iSO639_2);
        Assert.assertNull(iSO639_1);
    }

}
