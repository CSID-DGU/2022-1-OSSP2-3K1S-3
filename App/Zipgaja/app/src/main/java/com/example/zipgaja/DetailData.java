package com.example.zipgaja;

import java.io.Serializable;

public class DetailData implements Serializable {
    private String good1;
    private String good2;
    private String good3;
    private String good4;
    private String good_check1;
    private String good_check2;
    private String bad1;
    private String bad2;
    private String bad3;
    private String bad4;
    private String bad_check1;
    private String bad_check2;

    public String getBad3 ()
    {
        return bad3;
    }

    public void setBad3 (String bad3)
    {
        this.bad3 = bad3;
    }

    public String getBad4 ()
    {
        return bad4;
    }

    public void setBad4 (String bad4)
    {
        this.bad4 = bad4;
    }

    public String getBad1 ()
    {
        return bad1;
    }

    public void setBad1 (String bad1)
    {
        this.bad1 = bad1;
    }

    public String getBad2 ()
    {
        return bad2;
    }

    public void setBad2 (String bad2)
    {
        this.bad2 = bad2;
    }

    public String getGood4 ()
    {
        return good4;
    }

    public void setGood4 (String good4)
    {
        this.good4 = good4;
    }

    public String getGood3 ()
    {
        return good3;
    }

    public void setGood3 (String good3)
    {
        this.good3 = good3;
    }

    public String getGood2 ()
    {
        return good2;
    }

    public void setGood2 (String good2)
    {
        this.good2 = good2;
    }

    public String getGood1 ()
    {
        return good1;
    }

    public void setGood1 (String good1)
    {
        this.good1 = good1;
    }

    public String getGood_check2 ()
    {
        return good_check2;
    }

    public void setGood_check2 (String good_check2)
    {
        this.good_check2 = good_check2;
    }

    public String getBad_check1 ()
    {
        return bad_check1;
    }

    public void setBad_check1 (String bad_check1)
    {
        this.bad_check1 = bad_check1;
    }

    public String getGood_check1 ()
    {
        return good_check1;
    }

    public void setGood_check1 (String good_check1)
    {
        this.good_check1 = good_check1;
    }

    public String getBad_check2 ()
    {
        return bad_check2;
    }

    public void setBad_check2 (String bad_check2)
    {
        this.bad_check2 = bad_check2;
    }

    @Override
    public String toString()
    {
        return "[good1 = "+good1+", good2 = "+good2+", good3 = "+good3+", good4 = "+good4+", bad1 = "+bad1+", bad2 = "+bad2+", bad3 = "+bad3+", bad4 = "+bad4+"]";
    }
}
