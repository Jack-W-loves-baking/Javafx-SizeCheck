/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package itemsizecheck;

/**
 *
 * @author jack1
 */
enum BagSize 
{ 
    ExtraSmall(130,235), Small(165,235), Medium(255,325), Large(260,385), XLarge(395,455), XXLarge(420,594), Uber(600,605); 

    /**
     * @return the shortLength
     */
    public double getShortLength() {
        return shortLength;
    }

    /**
     * @return the longLength
     */
    public double getLongLength() {
        return longLength;
    }

   
    private final double shortLength;
    private final double longLength;
    
    BagSize(double shortLength, double longLength) {
        this.shortLength = shortLength;
        this.longLength = longLength;
    }
} 


  