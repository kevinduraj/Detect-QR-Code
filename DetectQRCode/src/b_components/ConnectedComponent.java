package b_components;

public class ConnectedComponent {

    private final int MAXCCLABEL = 32767;
    private List assoc_list[];
    private int assoc_labels[];

    private int lines = 0;
    private int samples = 0;

    private int[][] inputImage;

    public ConnectedComponent() {
        // -- create array of linked lists
        assoc_list = new List[MAXCCLABEL];
        for (int i = 0; i < MAXCCLABEL; ++i) {
            assoc_list[i] = new List();
        }

        assoc_labels = new int[MAXCCLABEL];
    }

    /*---------------------------------------------------------------------------------------------
        In place connected components --
        Really should be preceeded by a function that binarizes
        the image into foreground/background pieces.
        Find minimum label of surrounding labelled points. 
        Looks at the pattern...
            X X X
                X .
        These are the pixels that have been previously assigned labels
        Used by ConnectedComponentsInPlace()
    ----------------------------------------------------------------------------------------------*/
    int FindMinLabelA(int[][] _image, int i, int j, int _highest_label, int _background) {
        int minimum;

        minimum = _highest_label + 1;
        if ((_image[i - 1][j - 1] > _background) && (_image[i - 1][j - 1] < minimum)) {
            minimum = _image[i - 1][j - 1];
        }
        if ((_image[i - 1][j] > _background) && (_image[i - 1][j] < minimum)) {
            minimum = _image[i - 1][j];
        }
        if ((_image[i - 1][j + 1] > _background) && (_image[i - 1][j + 1] < minimum)) {
            minimum = _image[i - 1][j + 1];
        }
        if ((_image[i][j - 1] > _background) && (_image[i][j - 1] < minimum)) {
            minimum = _image[i][j - 1];
        }

        return minimum;
    }

    // -- Associate non-minimum labels to the minimum label. --
    //    Looks at the pattern...
    //			X X X
    //			X .
    //    These are the pixels that have been previously assigned labels
    //    Used by ConnectedComponentsInPlace()
    void AssociateNonminLabelsA(int[][] _image, int i, int j, int minimum, int _background) {
        if ((_image[i - 1][j - 1] != minimum) && (_image[i - 1][j - 1] > _background)) {
            Integer ii = new Integer(_image[i - 1][j - 1]);
            assoc_list[minimum].insertAtFront(ii);
            ii = new Integer(minimum);
            assoc_list[_image[i - 1][j - 1]].insertAtFront(ii);
        }

        if ((_image[i - 1][j] != minimum) && (_image[i - 1][j] > _background)) {
            Integer ii = new Integer(_image[i - 1][j]);
            assoc_list[minimum].insertAtFront(ii);
            ii = new Integer(minimum);
            assoc_list[_image[i - 1][j]].insertAtFront(ii);
        }

        if ((_image[i - 1][j + 1] != minimum) && (_image[i - 1][j + 1] > _background)) {
            Integer ii = new Integer(_image[i - 1][j + 1]);
            assoc_list[minimum].insertAtFront(ii);
            ii = new Integer(minimum);
            assoc_list[_image[i - 1][j + 1]].insertAtFront(ii);
        }

        if ((_image[i][j - 1] != minimum) && (_image[i][j - 1] > _background)) {
            Integer ii = new Integer(_image[i][j - 1]);
            assoc_list[minimum].insertAtFront(ii);
            ii = new Integer(minimum);
            assoc_list[_image[i][j - 1]].insertAtFront(ii);
        }
    }

    /*---------------------------------------------------------------------------------------------
     In place connected components --
     Should be preceeded by a function that binarizes 
     the image into foreground/background pieces.
     ----------------------------------------------------------------------------------------------*/
    public int Apply(int[][] _image, int _height, int _width, int _background) {

        int i, j;
        int highest_label, minimum;
        boolean pBit[];
        int index, newlabel;
        boolean done;
        int rowStart, rowEnd, colStart, colEnd;

        rowStart = 1;
        rowEnd = _height - 1;
        colStart = 1;
        colEnd = _width - 1;

        // black out the border pixels
        for (i = rowStart - 1; i <= rowEnd; ++i) {
            _image[i][colStart - 1] = 0;
            _image[i][colEnd] = 0;
        }
        for (j = colStart - 1; j <= colEnd; ++j) {
            _image[rowStart - 1][j] = 0;
            _image[rowEnd][j] = 0;
        }

        highest_label = _background;
        for (i = rowStart; i < rowEnd; ++i) {
            for (j = colStart; j < colEnd; ++j) {

                // --  Check if image point is object or background.
                if (_image[i][j] > _background) {

                    // -- Find minimum label of surrounding labelled points. 
                    minimum = FindMinLabelA(_image, i, j, highest_label, _background);

                    // -- Assign minimum surrounding label to current point. 
                    _image[i][j] = minimum;
                    if (minimum == highest_label + 1) {
                        ++highest_label;
                    }

                    // -- too many objects - clean up and return error
                    if (highest_label == MAXCCLABEL) {
                        for (i = 0; i < MAXCCLABEL; ++i) {
                            assoc_list[i].clearList();
                        }
                        return (-1);
                    }

                    // -- Associate non-minimum labels to the minimum label.
                    AssociateNonminLabelsA(_image, i, j, minimum, _background);
                }
            }
        }

        // -- minimize association (equivalence) table
        //    (from Horowitz's "data structures" book)
        pBit = new boolean[MAXCCLABEL + 1];
        for (i = 0; i < MAXCCLABEL + 1; ++i) {
            pBit[i] = false;
        }

        ListNode ptr, t, top, aPtr;

        newlabel = 0;
        index = 1;
        while (index < (highest_label + 1)) {
            if (!pBit[index]) {
                ++newlabel;
                assoc_labels[index] = newlabel;
                pBit[index] = true;
                ptr = assoc_list[index].firstNode;
                top = null;
                done = false;
                while (!done) {
                    while (ptr != null) {
                        Integer ix = (Integer) ptr.getObject();
                        j = ix.intValue();
                        if (!pBit[j]) {
                            assoc_labels[j] = newlabel;
                            pBit[j] = true;
                            t = ptr.nextNode;
                            ptr.nextNode = top;
                            top = ptr;
                            ptr = t;
                        } else {
                            // -- remove ptr from list
                            //			    aPtr = ptr;
                            ptr = ptr.nextNode;
                            //			    delete aPtr;
                        }
                    }
                    if (top == null) {
                        done = true;
                    } else {
                        Integer ix = (Integer) top.getObject();
                        int v = ix.intValue();
                        ptr = assoc_list[v].firstNode; // ?????????
                        //			aPtr = top;
                        top = top.nextNode;
                        //			delete aPtr;
                    }
                }
            }
            ++index;
        }

        // -- assign final labels
        for (i = rowStart; i < rowEnd; ++i) {
            for (j = colStart; j < colEnd; ++j) {
                _image[i][j] = (_image[i][j] > _background) ? assoc_labels[_image[i][j]] : 0;
            }
        }

        return newlabel;
    }

    public void ReformatImage(byte[] _image, int _lines, int _samples) {
        lines = _lines;
        samples = _samples;
        inputImage = new int[lines][samples];
        for (int i = 0; i < lines; ++i) {
            for (int j = 0; j < samples; ++j) {
                if (_image[i * samples + j] < 0) {
                    inputImage[i][j] = (int) _image[i * samples + j] + 255;
                } else {
                    inputImage[i][j] = (int) _image[i * samples + j];
                }
            }
        }
    }
}
