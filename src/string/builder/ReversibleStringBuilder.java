package string.builder;

import java.io.Serializable;
import java.util.Stack;
import java.util.stream.IntStream;

/***
 * string.builder.ReversibleStringBuilder like class with undo action support
 */
public final class ReversibleStringBuilder implements Serializable, Appendable, CharSequence, Comparable<ReversibleStringBuilder> {

    private static interface Reversible {
        void Reverse();
    }

    static final long serialVersionUID = -1L; // serializable ??????????????????

    /**
     * Save the state of the {@code string.builder.ReversibleStringBuilder} instance to a stream
     * (that is, serialize it).
     *
     * @serialData the number of characters currently stored in the string
     * builder ({@code int}), followed by the characters in the
     * string builder ({@code char[]}).   The length of the
     * {@code char} array may be greater than the number of
     * characters currently stored in the string builder, in which
     * case extra characters are ignored.
     */
    private void writeObject(java.io.ObjectOutputStream s)
            throws java.io.IOException {
        // TODO
    }

    /**
     * readObject is called to restore the state of the StringBuffer from
     * a stream.
     */
    private void readObject(java.io.ObjectInputStream s)
            throws java.io.IOException, ClassNotFoundException {
        // TODO
    }

    /**
     * Returns the length of this character sequence.  The length is the number
     * of 16-bit {@code char}s in the sequence.
     *
     * @return the number of {@code char}s in this sequence
     */
    @Override
    public int length() {
        return internalSB.length();
    }

    /**
     * Answers an integer hash code for the receiver. Any two
     * objects which answer <code>true</code> when passed to
     * <code>.equals</code> must answer the same value for this
     * method.
     *
     * @return the receiver's hash.
     * @see #equals
     */
    @Override
    public int hashCode() {
        return internalSB.hashCode();
    }

    /**
     * Compares the argument to the receiver, and answers true
     * if they represent the <em>same</em> object using a class
     * specific comparison. The implementation in Object answers
     * true only if the argument is the exact same object as the
     * receiver (==).
     *
     * @param o Object
     *          the object to compare with this object.
     * @return boolean <code>true</code>
     * if the object is the same as this object
     * <code>false</code>
     * if it is different from this object.
     * @see #hashCode
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof ReversibleStringBuilder) {
            return 0 == internalSB.compareTo(((ReversibleStringBuilder) o).internalSB);
        }
        return false;
    }

    /**
     * Returns the {@code char} value at the specified index.  An index ranges from zero
     * to {@code length() - 1}.  The first {@code char} value of the sequence is at
     * index zero, the next at index one, and so on, as for array
     * indexing.
     *
     * <p>If the {@code char} value specified by the index is a
     * <a href="{@docRoot}/java.base/java/lang/Character.html#unicode">surrogate</a>, the surrogate
     * value is returned.
     *
     * @param index the index of the {@code char} value to be returned
     * @return the specified {@code char} value
     * @throws IndexOutOfBoundsException if the {@code index} argument is negative or not less than
     *                                   {@code length()}
     */
    @Override
    public char charAt(int index) {
        return internalSB.charAt(index);
    }

    /**
     * Returns a {@code CharSequence} that is a subsequence of this sequence.
     * The subsequence starts with the {@code char} value at the specified index and
     * ends with the {@code char} value at index {@code end - 1}.  The length
     * (in {@code char}s) of the
     * returned sequence is {@code end - start}, so if {@code start == end}
     * then an empty sequence is returned.
     *
     * @param start the start index, inclusive
     * @param end   the end index, exclusive
     * @return the specified subsequence
     * @throws IndexOutOfBoundsException if {@code start} or {@code end} are negative,
     *                                   if {@code end} is greater than {@code length()},
     *                                   or if {@code start} is greater than {@code end}
     */
    @Override
    public CharSequence subSequence(int start, int end) {
        return internalSB.subSequence(start, end);
    }

    /**
     * Returns a stream of {@code int} zero-extending the {@code char} values
     * from this sequence.  Any char which maps to a <a
     * href="{@docRoot}/java.base/java/lang/Character.html#unicode">surrogate code
     * point</a> is passed through uninterpreted.
     *
     * <p>The stream binds to this sequence when the terminal stream operation
     * commences (specifically, for mutable sequences the spliterator for the
     * stream is <a href="../util/Spliterator.html#binding"><em>late-binding</em></a>).
     * If the sequence is modified during that operation then the result is
     * undefined.
     *
     * @return an IntStream of char values from this sequence
     * @since 1.8
     */
    @Override
    public IntStream chars() {
        return internalSB.chars();
    }

    /**
     * Returns a stream of code point values from this sequence.  Any surrogate
     * pairs encountered in the sequence are combined as if by {@linkplain
     * Character#toCodePoint Character.toCodePoint} and the result is passed
     * to the stream. Any other code units, including ordinary BMP characters,
     * unpaired surrogates, and undefined code units, are zero-extended to
     * {@code int} values which are then passed to the stream.
     *
     * <p>The stream binds to this sequence when the terminal stream operation
     * commences (specifically, for mutable sequences the spliterator for the
     * stream is <a href="../util/Spliterator.html#binding"><em>late-binding</em></a>).
     * If the sequence is modified during that operation then the result is
     * undefined.
     *
     * @return an IntStream of Unicode code points from this sequence
     * @since 1.8
     */
    @Override
    public IntStream codePoints() {
        return internalSB.codePoints();
    }

    private final StringBuilder internalSB;
    private final Stack<Reversible> operationHistory = new Stack<>();


    /**
     * Constructs a string builder with no characters in it and an
     * initial capacity of 16 characters.
     */
    public ReversibleStringBuilder() {
        this.internalSB = new StringBuilder();
    }

    /**
     * Constructs a string builder with no characters in it and an
     * initial capacity specified by the {@code capacity} argument.
     *
     * @param capacity the initial capacity.
     * @throws NegativeArraySizeException if the {@code capacity}
     *                                    argument is less than {@code 0}.
     */
    public ReversibleStringBuilder(int capacity) {
        this.internalSB = new StringBuilder(capacity);
    }

    /**
     * Constructs a string builder initialized to the contents of the
     * specified string. The initial capacity of the string builder is
     * {@code 16} plus the length of the string argument.
     *
     * @param str the initial contents of the buffer.
     */
    public ReversibleStringBuilder(String str) {
        this.internalSB = new StringBuilder(str);
    }

    /**
     * Constructs a string builder that contains the same characters
     * as the specified {@code CharSequence}. The initial capacity of
     * the string builder is {@code 16} plus the length of the
     * {@code CharSequence} argument.
     *
     * @param seq the sequence to copy.
     */
    public ReversibleStringBuilder(CharSequence seq) {
        this.internalSB = new StringBuilder(seq);
    }

    /**
     * Compares two {@code string.builder.ReversibleStringBuilder} instances lexicographically. This method
     * follows the same rules for lexicographical comparison as defined in the
     * {@linkplain java.lang.CharSequence#compare(java.lang.CharSequence,
     * java.lang.CharSequence)  CharSequence.compare(this, another)} method.
     *
     * <p>
     * For finer-grained, locale-sensitive String comparison, refer to
     * {@link java.text.Collator}.
     *
     * @param another the {@code string.builder.ReversibleStringBuilder} to be compared with
     * @return the value {@code 0} if this {@code string.builder.ReversibleStringBuilder} contains the same
     * character sequence as that of the argument {@code string.builder.ReversibleStringBuilder}; a negative integer
     * if this {@code string.builder.ReversibleStringBuilder} is lexicographically less than the
     * {@code string.builder.ReversibleStringBuilder} argument; or a positive integer if this {@code string.builder.ReversibleStringBuilder}
     * is lexicographically greater than the {@code string.builder.ReversibleStringBuilder} argument.
     * @since 11
     */
    public int compareTo(ReversibleStringBuilder another) {
        return this.internalSB.compareTo(another.internalSB);
    }

    public ReversibleStringBuilder append(Object obj) {
        return insert(internalSB.length(), String.valueOf(obj));
    }

    public ReversibleStringBuilder append(String str) {
        return append((Object) str);
    }

    /**
     * Appends the specified {@code StringBuffer} to this sequence.
     * <p>
     * The characters of the {@code StringBuffer} argument are appended,
     * in order, to this sequence, increasing the
     * length of this sequence by the length of the argument.
     * If {@code sb} is {@code null}, then the four characters
     * {@code "null"} are appended to this sequence.
     * <p>
     * Let <i>n</i> be the length of this character sequence just prior to
     * execution of the {@code append} method. Then the character at index
     * <i>k</i> in the new character sequence is equal to the character at
     * index <i>k</i> in the old character sequence, if <i>k</i> is less than
     * <i>n</i>; otherwise, it is equal to the character at index <i>k-n</i>
     * in the argument {@code sb}.
     *
     * @param sb the {@code StringBuffer} to append.
     * @return a reference to this object.
     */
    public ReversibleStringBuilder append(StringBuffer sb) {
        return append((Object) sb);
    }

    public ReversibleStringBuilder append(CharSequence s) {
        return append((Object) s);
    }

    /**
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public ReversibleStringBuilder append(CharSequence s, int start, int end) {
        return append(String.valueOf(s).subSequence(start, end));
    }

    public ReversibleStringBuilder append(char[] str) {
        return insert(length(), str);
    }

    /**
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public ReversibleStringBuilder append(char[] str, int offset, int len) {
        return append(String.valueOf(str, offset, len));
    }

    public ReversibleStringBuilder append(boolean b) {
        return append(Boolean.toString(b));
    }

    public ReversibleStringBuilder append(char c) {
        return append(Character.toString(c));
    }

    public ReversibleStringBuilder append(int i) {
        return append(Integer.toString(i));
    }

    public ReversibleStringBuilder append(long lng) {
        return append(Long.toString(lng));
    }

    public ReversibleStringBuilder append(float f) {
        return append(Float.toString(f));
    }

    public ReversibleStringBuilder append(double d) {
        return append(Double.toString(d));
    }

    /**
     * @since 1.5
     */
    public ReversibleStringBuilder appendCodePoint(int codePoint) {
        return append(Character.toChars(codePoint));
    }

    /**
     * @throws StringIndexOutOfBoundsException {@inheritDoc}
     */
    public ReversibleStringBuilder delete(int start, int end) {
        return replace(start, end, "");
    }

    /**
     * @throws StringIndexOutOfBoundsException {@inheritDoc}
     */
    public ReversibleStringBuilder deleteCharAt(int index) {
        return delete(index, index + 1);
    }

    /**
     * @throws StringIndexOutOfBoundsException {@inheritDoc}
     */
    public ReversibleStringBuilder replace(int start, int end, String str) {
        String deletedString = internalSB.substring(start, end);
        Reversible reverseDelOp = (start == end)
                ? () -> {
        }
                : () -> {
            internalSB.insert(start, deletedString);
        };

        str = String.valueOf(str); // in case when str eq to NULL
        int strLen = str.length();
        Reversible reverseInsOp = (strLen == 0) ? () -> {
        } : () -> {
            internalSB.delete(start, start + strLen);
        };

        operationHistory.push(() -> {
            reverseDelOp.Reverse();
            reverseInsOp.Reverse();
        });

        internalSB.replace(start, end, str);
        return this;
    }

    /**
     * @throws StringIndexOutOfBoundsException {@inheritDoc}
     */
    public ReversibleStringBuilder insert(int index, char[] str, int offset, int len) {
        return insert(index, String.valueOf(str, offset, len));
    }

    /**
     * @throws StringIndexOutOfBoundsException {@inheritDoc}
     */
    public ReversibleStringBuilder insert(int offset, Object obj) {
        return replace(offset, offset, String.valueOf(obj));
    }

    /**
     * @throws StringIndexOutOfBoundsException {@inheritDoc}
     */
    public ReversibleStringBuilder insert(int offset, String str) {
        return replace(offset, offset, str);
    }

    /**
     * @throws StringIndexOutOfBoundsException {@inheritDoc}
     */
    public ReversibleStringBuilder insert(int offset, char[] str) {
        return replace(offset, offset, String.valueOf(str));
    }

    /**
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public ReversibleStringBuilder insert(int dstOffset, CharSequence s) {
        return insert(dstOffset, s.toString());
    }

    /**
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public ReversibleStringBuilder insert(int dstOffset, CharSequence s,
                                          int start, int end) {
        return insert(dstOffset, s.subSequence(start, end));
    }

    /**
     * @throws StringIndexOutOfBoundsException {@inheritDoc}
     */
    public ReversibleStringBuilder insert(int offset, boolean b) {
        return insert(offset, Boolean.toString(b));
    }

    /**
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public ReversibleStringBuilder insert(int offset, char c) {
        return insert(offset, Character.toString(c));
    }

    /**
     * @throws StringIndexOutOfBoundsException {@inheritDoc}
     */
    public ReversibleStringBuilder insert(int offset, int i) {
        return insert(offset, Integer.toString(i));
    }

    /**
     * @throws StringIndexOutOfBoundsException {@inheritDoc}
     */
    public ReversibleStringBuilder insert(int offset, long l) {
        return insert(offset, Long.toString(l));
    }

    /**
     * @throws StringIndexOutOfBoundsException {@inheritDoc}
     */
    public ReversibleStringBuilder insert(int offset, float f) {
        return insert(offset, Float.toString(f));
    }

    /**
     * @throws StringIndexOutOfBoundsException {@inheritDoc}
     */
    public ReversibleStringBuilder insert(int offset, double d) {
        return insert(offset, Double.toString(d));
    }

    public int indexOf(String str) {
        return internalSB.indexOf(str);
    }

    public int indexOf(String str, int fromIndex) {
        return internalSB.indexOf(str, fromIndex);
    }

    public int lastIndexOf(String str) {
        return internalSB.lastIndexOf(str);
    }

    public int lastIndexOf(String str, int fromIndex) {
        return internalSB.lastIndexOf(str, fromIndex);
    }

    public ReversibleStringBuilder reverse() {
        operationHistory.push(() -> {
            internalSB.reverse();
        });
        internalSB.reverse();
        return this;
    }

    public String toString() {
        return internalSB.toString();
    }

    public int capacity() {
        return internalSB.capacity();
    }

    public int codePointAt(int index) {
        return internalSB.codePointAt(index);
    }

    public int codePointBefore(int index) {
        return codePointBefore(index);
    }

    public int codePointCount(int beginIndex, int endIndex) {
        return codePointCount(beginIndex, endIndex);
    }

    public int offsetByCodePoints(int index, int codePointOffset) {
        return offsetByCodePoints(index, codePointOffset);
    }

    public String substring(int start, int end) {
        return substring(start, end);
    }

    public String substring(int start) {
        return substring(start);
    }

    public void ensureCapacity(int minimumCapacity) {
        ensureCapacity(minimumCapacity);
    }

    public void getChars(int srcBegin, int srcEnd, char[] dst, int dstBegin) {
        internalSB.getChars(srcBegin, srcEnd, dst, dstBegin);
    }

    public void setCharAt(int index, char ch) {
        replace(index, index + 1, String.valueOf(ch));
    }

    public void setLength(int newLength) {
        int len = length();
        String nullChStr = String.valueOf('\u0000');

        if (len > newLength) {
            replace(len, newLength, nullChStr.repeat(newLength - len));
        } else if (len < newLength) {
            append(nullChStr.repeat(len - newLength));
        }

        internalSB.setLength(newLength);
    }

    public void trimToSize() {
        internalSB.trimToSize();
    }

    public void Undo() {
        if (operationHistory.size() > 0) {
            operationHistory.pop().Reverse();
        }
    }
}
