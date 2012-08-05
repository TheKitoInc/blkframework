/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package BLK.System.Utils;

/**
 *
 * @author andresrg
 */
public class Pair
{
        private String name=null;
        private Object value=null;

        public Pair(String name, Object value)
        {
            this.name = name;
            this.value = value;
        }

        public Pair(String line)
        {
            String d[]=line.split(":", 2);

            if(d.length>0)
                this.name=d[0].trim();

            if(d.length>1)
                this.value=d[1].trim();
        }

        @Override
        public String toString()
        {
            return this.name+": "+this.value.toString();
        }

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public Object getValue() {return value;}
    public void setValue(Object value) {this.value = value;}

     public Integer getInteger() throws ClassCastException
    {
        if(this.value==null)
            return null;

        return ((Integer)this.value);
    }   
    public String getString()
    {
        if(this.value==null)
            return null;

        return (this.value.toString());
    }


}

