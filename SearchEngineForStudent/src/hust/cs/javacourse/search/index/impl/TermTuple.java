package hust.cs.javacourse.search.index.impl;

import java.util.Objects;
import hust.cs.javacourse.search.index.AbstractTermTuple;

public class TermTuple extends AbstractTermTuple{
    
    public TermTuple()
    {
        super();
    }

    public TermTuple(AbstractTerm term, int curPos)
    {
        super(term, curPos);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof AbstractTermTuple)
        {
            AbstractTermTuple termTuple = (AbstractTermTuple) obj;
            return Objects.equals(this.term, termTuple.term) && this.curPos == termTuple.curPos;
        }
        return false;
    }

    @Override
    public String toString()
    {
        return this.term.toString() + " " + this.freq + " " + this.curPos;
    }
}
