package com.pr.boostyou.api.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class InstaProfile implements Serializable {

	@SerializedName("logging_page_id")
	private String loggingPageId;

	@SerializedName("graphql")
	private Graphql graphql = new Graphql();

	public String getLoggingPageId() {
		return loggingPageId;
	}

	public void setLoggingPageId(String loggingPageId) {
		this.loggingPageId = loggingPageId;
	}

	public class Graphql implements Serializable{
		@SerializedName("user")
		private User2 user = new User2();

		public User2 getUser() {
			return user;
		}

		public void setUser(User2 user) {
			this.user = user;
		}
	}


	public String getId(){
		return this.getGraphql().getUser().getId();
	}

	public String  getUserName(){
		return this.getGraphql().getUser().getUserName();
	}

	public class User2 implements Serializable{
		@SerializedName("id")
		private String id = "";
		@SerializedName("full_name")
		private String fullName;
		@SerializedName("profile_pic_url")
		private String profilePictureUrl;
		@SerializedName("username")
		private String userName;

		@SerializedName("edge_owner_to_timeline_media")
		private EdgeOwnerToTimeLineMedia edgeOwnerToTimeLineMedia = new EdgeOwnerToTimeLineMedia();

		@SerializedName("edge_followed_by")
		private EdgeFollowedBy edgeFollowedBy = new EdgeFollowedBy();

		public EdgeFollowedBy getEdgeFollowedBy() {
			return edgeFollowedBy;
		}

		public String getId() {
			return id;
		}

		public String getFullName() {
			return fullName;
		}

		public String getUserName() {
			return userName;
		}

		public void setFullName(String fullName) {
			this.fullName = fullName;
		}

		public String getProfilePictureUrl() {
			return profilePictureUrl;
		}

		public void setProfilePictureUrl(String profilePictureUrl) {
			this.profilePictureUrl = profilePictureUrl;
		}

		public EdgeOwnerToTimeLineMedia getEdgeOwnerToTimeLineMedia() {
			return edgeOwnerToTimeLineMedia;
		}
	}

	public class EdgeOwnerToTimeLineMedia implements Serializable{
		@SerializedName("count")
		private int count;

		@SerializedName("edges")
		private List<Edges> edgesList;

		public int getCount() {
			return count;
		}

		public List<Edges> getEdgesList() {
			return edgesList;
		}
	}

	public class EdgeFollowedBy implements Serializable{
		@SerializedName("count")
		private int followCount;

		public int getFollowCount() {
			return followCount;
		}

	}

	public class Edges implements Serializable{
		@SerializedName("node")
		private Node node;

		public Node getNode() {
			return node;
		}
	}


	public class Node implements Serializable{
		@SerializedName("shortcode")
		private String shortCode;

		@SerializedName("display_url")
		private String displayUrl;

		@SerializedName("is_video")
		private boolean isVideo;

		@SerializedName("thumbnail_src")
		private String thumbnailUrl;

		@SerializedName("edge_liked_by")
		private EdgeLikedBy edgeLikedBy;

		@SerializedName("edge_media_to_comment")
		private EdgeMediaToComment edgeMediaToComment;

		public String getThumbnailUrl() {
			return thumbnailUrl;
		}

		public String getDisplayUrl() {
			return displayUrl;
		}

		public boolean isVideo() {
			return isVideo;
		}

		public EdgeLikedBy getEdgeLikedBy() {
			return edgeLikedBy;
		}

		public EdgeMediaToComment getEdgeMediaToComment() {
			return edgeMediaToComment;
		}

		public String getShortCode() {
			return shortCode;
		}
	}

	public class EdgeLikedBy implements Serializable{
		@SerializedName("count")
		private int likeCount = -1;

		public int getLikeCount() {
			return likeCount;
		}
	}
	public class EdgeMediaToComment implements Serializable{
		@SerializedName("count")
		private int commentCount = -1;

		public int getCommentCount() {
			return commentCount;
		}
	}



	public Graphql getGraphql() {
		return graphql;
	}

	public void setGraphql(Graphql graphql) {
		this.graphql = graphql;
	}

}
