import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import java.util.Stack;

/***
 * ReversibleStringBuilder like class with undo action support
 */
public final class ReversibleStringBuilder implements Serializable, Appendable, CharSequence, Comparable<ReversibleStringBuilder> {

    private interface Reversible {
        public void Reverse();
    }

    static final long serialVersionUID = -1L; // serializable ??????????????????

    private final StringBuilder internalSB;
    private final Stack<Reversible> operationHistory = new Stack<>();

    private static final char[] DEFAULT_CHAR_ARRAY_VALUE = new char[]{'n', 'u', 'l', 'l'};
    private static final String DEFAULT_STRING_VALUE = "null";

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
     * Compares two {@code ReversibleStringBuilder} instances lexicographically. This method
     * follows the same rules for lexicographical comparison as defined in the
     * {@linkplain java.lang.CharSequence#compare(java.lang.CharSequence,
     * java.lang.CharSequence)  CharSequence.compare(this, another)} method.
     *
     * <p>
     * For finer-grained, locale-sensitive String comparison, refer to
     * {@link java.text.Collator}.
     *
     * @param another the {@code ReversibleStringBuilder} to be compared with
     * @return the value {@code 0} if this {@code ReversibleStringBuilder} contains the same
     * character sequence as that of the argument {@code ReversibleStringBuilder}; a negative integer
     * if this {@code ReversibleStringBuilder} is lexicographically less than the
     * {@code ReversibleStringBuilder} argument; or a positive integer if this {@code ReversibleStringBuilder}
     * is lexicographically greater than the {@code ReversibleStringBuilder} argument.
     * @since 11
     */
    public int compareTo(ReversibleStringBuilder another) {
        return this.internalSB.compareTo(another.internalSB);
    }

    public ReversibleStringBuilder append(Object obj) {
        return insert(internalSB.length(), Objects.requireNonNullElse(obj, DEFAULT_STRING_VALUE).toString());
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
        return append(Objects.requireNonNullElse(s, DEFAULT_STRING_VALUE).subSequence(start, end));
    }

    public ReversibleStringBuilder append(char[] str) {
        return insert(length(),str);
    }

    /**
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public ReversibleStringBuilder append(char[] str, int offset, int len) {
        return append(Arrays.copyOfRange(Objects.requireNonNullElse(str, DEFAULT_CHAR_ARRAY_VALUE), offset, offset + len));
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
        return replace(start,end,"");
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

        str = Objects.requireNonNullElse(str, DEFAULT_STRING_VALUE);
        int strLen = str.length();
        Reversible reverseInsOp = (strLen==0) ? () -> {
        } : () -> {
            internalSB.delete(start, start+strLen);
        };

        operationHistory.push(() -> {
            reverseDelOp.Reverse();
            reverseInsOp.Reverse();
        });

        internalSB.replace(start,end,str);
        return this;
    }

    /**
     * @throws StringIndexOutOfBoundsException {@inheritDoc}
     */
    public ReversibleStringBuilder insert(int index, char[] str, int offset,
                                          int len) {
//TODO
    }

    /**
     * @throws StringIndexOutOfBoundsException {@inheritDoc}
     */
    public ReversibleStringBuilder insert(int offset, Object obj) {

    }

    /**
     * @throws StringIndexOutOfBoundsException {@inheritDoc}
     */
    public ReversibleStringBuilder insert(int offset, String str) {
        int start = offset;
        int end = start + str.length();
        return replace(start, end, str);
    }

    /**
     * @throws StringIndexOutOfBoundsException {@inheritDoc}
     */
    public ReversibleStringBuilder insert(int offset, char[] str) {

    }

    /**
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public ReversibleStringBuilder insert(int dstOffset, CharSequence s) {

    }

    /**
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public ReversibleStringBuilder insert(int dstOffset, CharSequence s,
                                          int start, int end) {

    }

    /**
     * @throws StringIndexOutOfBoundsException {@inheritDoc}
     */
    public ReversibleStringBuilder insert(int offset, boolean b) {

    }

    /**
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public ReversibleStringBuilder insert(int offset, char c) {

    }

    /**
     * @throws StringIndexOutOfBoundsException {@inheritDoc}
     */
    public ReversibleStringBuilder insert(int offset, int i) {

    }

    /**
     * @throws StringIndexOutOfBoundsException {@inheritDoc}
     */
    public ReversibleStringBuilder insert(int offset, long l) {

    }

    /**
     * @throws StringIndexOutOfBoundsException {@inheritDoc}
     */
    public ReversibleStringBuilder insert(int offset, float f) {

    }

    /**
     * @throws StringIndexOutOfBoundsException {@inheritDoc}
     */
    public ReversibleStringBuilder insert(int offset, double d) {

    }

    public int indexOf(String str) {

    }

    public int indexOf(String str, int fromIndex) {

    }

    public int lastIndexOf(String str) {

    }

    public int lastIndexOf(String str, int fromIndex) {

    }

    public ReversibleStringBuilder reverse() {

    }

    public String toString() {

    }

    /**
     * Save the state of the {@code ReversibleStringBuilder} instance to a stream
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

    }

    /**
     * readObject is called to restore the state of the StringBuffer from
     * a stream.
     */
    private void readObject(java.io.ObjectInputStream s)
            throws java.io.IOException, ClassNotFoundException {

    }
}
