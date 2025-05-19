package com.example.lab1;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.preference.PreferenceManager;

import com.google.gson.JsonSyntaxException;

import org.json.JSONException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

public class CountryViewModel extends ViewModel{
    private MutableLiveData<List<Country>> fullCountryList = new MutableLiveData<>();
    private MutableLiveData<List<Country>> currentPageList = new MutableLiveData<>();
    private MutableLiveData<Boolean> isStarted = new MutableLiveData<>(false);

    private MutableLiveData<Boolean> isNextEnabled = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> isPrevEnabled = new MutableLiveData<>(false);

    public LiveData<Boolean> getIsNextEnabled() {
        return isNextEnabled;
    }

    public LiveData<Boolean> getIsPrevEnabled() {
        return isPrevEnabled;
    }

    private Context context; // Храним контекст
    private final Observer<List<Country>> countryObserver;

    private int currentPage = 0;
    private int pageSize = 10; // Количество элементов на странице


    public CountryViewModel(Context context) {
        this.context = context;

        countryObserver = countries -> {
            if (countries != null) {
                updatePageList();
            }
        };

        // Подписываемся на изменения fullCountryList
        fullCountryList.observeForever(countryObserver);
    }

    public LiveData<List<Country>> getCountryList() {
        return currentPageList;
    }


    public void loadCountries(String serverUrl) {
        if(!isStarted.getValue()) {
            isStarted.postValue(true);
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        String json = NetworkUtils.loadJsonFromUrl(serverUrl + "/data.json");
                        List<Country> countries = JSONHelper.parseJson(json);
                        fullCountryList.postValue(countries);
                        //updatePageList();
                    }
                    catch (JsonSyntaxException e) {
                        showToast("Неверный формат JSON");
                    }
                    catch (FileNotFoundException e) {
                        showToast("Неверный путь к серверу");
                    }
                    catch (MalformedURLException e) {
                        Log.e("CountryViewModel", "Неверный URL", e);
                        showToast("Указан некорректный URL");
                    } catch (SocketTimeoutException e) {
                        Log.e("CountryViewModel", "Тайм-аут запроса", e);
                        showToast("Сервер не отвечает. Попробуйте позже.");
                    } catch (UnknownHostException e) {
                        Log.e("CountryViewModel", "Нет подключения к интернету", e);
                        showToast("Проблема с подключением. Проверьте интернет.");
                    } catch (Exception e) {
                        Log.e("CountryViewModel", "Неизвестная ошибка", e);
                        showToast("Произошла ошибка. Попробуйте позже.");
                    } finally {
                        isStarted.postValue(false);
                    }
                }
            };

            Thread thread = new Thread(runnable);
            thread.start();
        }
    }


    public void nextPage() {
        if (fullCountryList.getValue() != null && (currentPage + 1) * pageSize < fullCountryList.getValue().size()) {
            currentPage++;
            updatePageList();
        }
    }

    public void prevPage() {
        if (currentPage > 0) {
            currentPage--;
            updatePageList();
        }
    }


    private int getPageSize() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String rowCount = sharedPreferences.getString("row_count", "all");

        if (fullCountryList.getValue() == null) {
            return 0;
        }

        if ("all".equals(rowCount)) {
            return fullCountryList.getValue().size();

        } else {
            int count = Integer.parseInt(rowCount);
            return Math.min(count, fullCountryList.getValue().size());
        }
    }

    void updatePageList() {

        int pSize = getPageSize();
        Log.d("UPDATE PAGE LIST", String.valueOf(pSize));
        pageSize = pSize > 0 ? pSize : pageSize;


        if (fullCountryList.getValue() != null) {
            while (currentPage * pageSize >= fullCountryList.getValue().size()) {
                currentPage--;
            }

            int start = currentPage * pageSize;
            int end = Math.min(start + pageSize, fullCountryList.getValue().size());
            currentPageList.postValue(fullCountryList.getValue().subList(start, end));

            // Обновляем состояние кнопок
            isPrevEnabled.postValue(currentPage > 0);
            isNextEnabled.postValue((currentPage + 1) * pageSize < fullCountryList.getValue().size());
        }
    }

    private void showToast(String message) {
        new Handler(Looper.getMainLooper()).post(() ->
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show());
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        fullCountryList.removeObserver(countryObserver); // Отписываемся
    }
}

