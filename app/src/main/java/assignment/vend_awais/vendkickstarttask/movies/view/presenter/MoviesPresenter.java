package assignment.vend_awais.vendkickstarttask.movies.view.presenter;

import java.util.List;

import javax.inject.Inject;

import assignment.vend_awais.vendkickstarttask.api.IWebServiceKickStart;
import assignment.vend_awais.vendkickstarttask.api.MoviesListResponseModel;
import assignment.vend_awais.vendkickstarttask.movies.model.Movie;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static assignment.vend_awais.vendkickstarttask.util.Constants.API_KEY;

public class MoviesPresenter implements Presenter, Observer<MoviesListResponseModel> {
    private PresenterView presenterView;
    @Inject
    IWebServiceKickStart iWebServiceKickStart;

    @Inject
    public MoviesPresenter() {

    }

    @Override
    public void onLoadMovies() {
        presenterView.showProgress();
        //movies.getCategories(this);
        getCategoriesViaObservable();
    }

    @Override
    public void setView(PresenterView presenterView) {
        this.presenterView = presenterView;
    }

    private void getCategoriesViaObservable() {
        Observable<MoviesListResponseModel> moviesObservable = iWebServiceKickStart.getMovieList(API_KEY);
        subscribe(moviesObservable, this);
    }

    @Override
    public void onItemSelected(Movie movie, int position) {
        presenterView.showMessage(String.format(movie.getTitle() + " ->" + " Position %d clicked", position));
    }

    @Override
    public void onCompleted() {
        presenterView.hideProgress();
    }

    @Override
    public void onError(Throwable e) {
        presenterView.showMessage(e.getMessage());
        presenterView.hideProgress();
    }

    @Override
    public void onNext(MoviesListResponseModel moviesListResponseModel) {
        presenterView.showCategories(moviesListResponseModel.getMovies());
    }

    public interface PresenterView {

        void showProgress();

        void hideProgress();

        void showCategories(List<Movie> categories);

        void showMessage(String message);
    }
}
