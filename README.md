### StudentManagement
Lưu ý khi sử dụng Observable
1. Đặt try catch ở ngoài Observable là dư thừa bởi vì: <br>
  +> Khi có bất cứ lỗi nào nó sẽ nhảy vào doOnError() và onError() bên trong subscribe()
2. Observable có quan tâm đến thứ tự gọi func cụ thể: <br>
  +> Theo function dưới đây thì các func trước .observeOn(AndroidSchedules.mainThread()) sẽ cùng sử dụng chung 1 Thread. Tùy theo operator mà có thể có nhiều
  thread khác nhau.<br>
  +> Theo thứ tự func doOnError() sau doOnNext() , doOnError() sau subcribeOn(), doOnError() sau observeOn() thì các xử lý sẽ được thực hiện
  trên các Thread khác nhau cụ thể: 2 func doOnError() đầu tiên sẽ chạy cùng 1 Thread và doOnError cuối cùng sẽ chạy trên MainThread cùng với onError.<br>
  Vậy khi xuất hiện lỗi thì ta gọi hàm doOnError cuối thay cho onError ở bên trong subcribe. (Nghe thì có vẻ hợp lý nhưng không)<br>
  ```java
  ---> Khi đó app sẽ bị Crash và thông báo lỗi:
  The exception was not handled due to missing onError handler in the subscribe() method call.
  // Lỗi này yêu cầu ta phải implement onError bên trong subscribe.
  ```
====> doOnError() khá không cần thiết, ta chỉ cần gọi onError bên trong onSubscribe là được, bởi vì khi có lỗi thì trong trường hợp này tôi muốn<br>
show ra 1 DiaLog thông báo lỗi, nên sử dụng onError là cực kì cần thiết nhằm đảm bảo 2 mục đính:
- App không bị Crash khi có Exception mà ta không implement onError.
- Tận dụng MainThread để cập nhật UI cụ thể là Dialog.<br>
```java
// try {
                        Observable
                                .just(new Grade("12AA", "LUN"))
                                .doOnNext(integer -> {
                                    Log.d("HomeFragment",
                                            "Emitting item " + integer + " on: "
                                                    + Thread.currentThread().getName());
                                    dao.insertGrade(integer);
                                })
                                .doOnError(throwable -> {
                                    Log.d("HomeFragment", "Error: " + throwable.getMessage());
                                    Log.d("HomeFragment", "Error CurrentThread1: " + 
                                                                            Thread.currentThread().getName());
                                })
                                .subscribeOn(Schedulers.computation())
                                // Do on Computation Thread
                                .doOnError(throwable -> {
                                    Log.d("HomeFragment", "Error After Subscribe on: " + throwable.getMessage());
                                    Log.d("HomeFragment", "Error After Subscribe on " + 
                                                                            Thread.currentThread().getName());
                                })
                                .observeOn(AndroidSchedulers.mainThread())
                                // Do on Computation Thread
                                .doOnError(throwable -> {
                                    Log.d("HomeFragment", "Error After observeOn: " + throwable.getMessage());
                                    Log.d("HomeFragment", "Error After observerOn: " + 
                                                                            Thread.currentThread().getName());
                                })
                                .subscribe(
                                        grade -> {},
                                        throwable -> Log.d("HomeFragment", "Error on subscribe: "
                                        + Thread.currentThread().getName())
                                );
                    } 
//                    catch (Exception e) {
//                        Log.d("HomeFragment", "Error: " + e.getMessage());
//                        Log.d("HomeFragment", "TryCatchError CurrentThread: " + Thread.currentThread().getName());
//                    }
```
