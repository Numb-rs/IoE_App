package internetofeveryone.ioe.Presenter;

import internetofeveryone.ioe.Data.DataType;

public interface ModelObserver {

    public void update(DataType type, String id);
}
