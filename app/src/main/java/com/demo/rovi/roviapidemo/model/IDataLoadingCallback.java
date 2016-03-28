package com.demo.rovi.roviapidemo.model;

/**
 * @author Alexey Kovalev
 * @since 28.03.2016.
 */
public interface IDataLoadingCallback<T> {

    void onResult(T loadedData);

    void onFailure(Throwable ex);
}
