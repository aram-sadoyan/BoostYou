package com.pr.boostyou.adaper;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.pr.boostyou.R;
import com.pr.boostyou.activity.PostsActivity;
import com.pr.boostyou.activity.ui.FrescoLoader;
import com.pr.boostyou.api.RestClient;
import com.pr.boostyou.api.model.BossLikeModel;
import com.pr.boostyou.api.model.InstaProfile;
import com.pr.boostyou.util.Utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.pr.boostyou.util.AppConstants.TK;


public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	//private List<String> itemUrls = new ArrayList<>();
	private int selectedPosition = 0;
	private FrescoLoader frescoLoader;
	private InstaProfile.EdgeOwnerToTimeLineMedia edgeOwnerToTimeLineMedia = null;
	private String curentAccesToken = null;
	private Activity context = null;

	List<InstaProfile.Edges> items = new ArrayList<>();

	Map<Integer, String> permaLinkMap = new HashMap<>();

	Map<String, String> savedIdTasks = new HashMap<>();


	private final PostsActivity.OnItemClickListener listener;


	public PostAdapter(
			Activity context,
			InstaProfile.EdgeOwnerToTimeLineMedia edgeOwnerToTimeLineMedia,
			String curentAccesToken,
			PostsActivity.OnItemClickListener onItemClickListener) {

		this.context = context;
		frescoLoader = new FrescoLoader();
		this.listener = onItemClickListener;

		this.edgeOwnerToTimeLineMedia = edgeOwnerToTimeLineMedia;

		this.items = edgeOwnerToTimeLineMedia.getEdgesList();

		this.curentAccesToken = curentAccesToken;
		savedIdTasks = Utils.loadMap("likes");
	}

	public String getSelectedPermanentLink(int position) {
		String permaLink = "";
		permaLink = permaLinkMap.get(position + 1);
		return permaLink;
	}

	@NonNull
	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.post_adapter_item, parent, false);
		return new PostAdapter.ItemViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull RecyclerView.ViewHolder itemViewHolder, int position) {
		ItemViewHolder holder = (ItemViewHolder) itemViewHolder;
		if (position == 0 || position > items.size()) {
			holder.postParentContainer.setVisibility(View.GONE);
			return;
		}
		InstaProfile.Edges item = items.get(position - 1);
		holder.postParentContainer.setVisibility(View.VISIBLE);

		//SHOWing POST IMG?VIDEO
		if (item.getNode().isVideo()) {
			frescoLoader.loadWithParams(Uri.parse(item.getNode().getThumbnailUrl()), holder.postImgView, false);
		} else {
			frescoLoader.loadWithParams(Uri.parse(item.getNode().getThumbnailUrl()), holder.postImgView, false);
		}

		holder.likeTxtView.setText(String.valueOf(item.getNode().getEdgeLikedBy().getLikeCount()));
		holder.commentTxtView.setText(String.valueOf(item.getNode().getEdgeMediaToComment().getCommentCount()));

	}

	private void requestInfoTaskForSettingProgress(String permaLink,
												   String idTask,
												   ItemViewHolder holder) {
		RestClient.getInstance(context).getInstaApiService()
				.getInfoTask(TK, idTask).enqueue(new Callback<BossLikeModel>() {
			@Override
			public void onResponse(Call<BossLikeModel> call, Response<BossLikeModel> response) {
				BossLikeModel bossLikeModel = response.body();
				if (bossLikeModel != null) {
					int count = Integer.parseInt(bossLikeModel.getData().getTask().getCount());
					int completeCount = Integer.parseInt(bossLikeModel.getData().getTask().getCountComplete());
					if (count == completeCount) {
						//removing idTask from map and save new
						//showing likes/comments

						savedIdTasks.remove(permaLink);
						Utils.saveMap("likes", savedIdTasks);

						holder.likesCommentContainer.setVisibility(View.VISIBLE);
						holder.loadingView.setVisibility(View.GONE);

					} else {
						//showing progress for idTask
						int completePercent = 100 * completeCount / count;
						holder.progressBar.setProgress(completePercent);
						holder.likesCommentContainer.setVisibility(View.GONE);
						holder.loadingView.setVisibility(View.VISIBLE);
					}

				}
			}

			@Override
			public void onFailure(Call<BossLikeModel> call, Throwable t) {
				//showing progress for idTask

				holder.likesCommentContainer.setVisibility(View.VISIBLE);
				holder.loadingView.setVisibility(View.GONE);

			}
		});
	}


	@Override
	public int getItemCount() {
		return items.size() + 2;
	}


	public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		private SimpleDraweeView postImgView;
		private TextView likeTxtView;
		private TextView commentTxtView;
		private CardView postParentContainer = null;
		private ConstraintLayout likesCommentContainer = null;
		private ConstraintLayout loadingView = null;
		private ProgressBar progressBar = null;


		ItemViewHolder(View view) {
			super(view);
			itemView.setOnClickListener(this);
			postImgView = view.findViewById(R.id.postImgView);
			postParentContainer = view.findViewById(R.id.postParentContainer);
			likeTxtView = view.findViewById(R.id.likeTxtView);
			commentTxtView = view.findViewById(R.id.commentTxtView);
			likesCommentContainer = view.findViewById(R.id.likesCommentContainer);
			loadingView = view.findViewById(R.id.loadingContainer);
			progressBar = view.findViewById(R.id.progressBar);
		}


		@Override
		public void onClick(View v) {


			if (getAdapterPosition() == RecyclerView.NO_POSITION) return;
			if (selectedPosition != getAdapterPosition()) {
				int adapterPosition = getAdapterPosition();
				//listener.onItemClick(2);
			}
//			notifyItemChanged(selectedPosition);
//			selectedPosition = getAdapterPosition();
//			notifyItemChanged(selectedPosition);
		}
	}
}
