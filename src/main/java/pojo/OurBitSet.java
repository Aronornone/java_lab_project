package pojo;

import java.util.BitSet;

/**
 * Variation of BitSet which does NOT interpret the highest bit synonymous with
 * its length.
 *
 * @author casper.bang@gmail.com
 */
public class OurBitSet extends BitSet{

    private int fixedLength;

    public OurBitSet(int fixedLength){
        super(fixedLength);
        this.fixedLength = fixedLength;
    }

    @Override
    public int length() {
        return fixedLength;
    }
}