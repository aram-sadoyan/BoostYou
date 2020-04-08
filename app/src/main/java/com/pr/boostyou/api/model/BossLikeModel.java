package com.pr.boostyou.api.model;

import com.google.gson.annotations.SerializedName;

public class BossLikeModel {

	@SerializedName("status")
	private String status;

	@SerializedName("success")
	private boolean success;

	@SerializedName("data")
	private Data data = new Data();

	public class Data{

		@SerializedName("task")
		private Task task = new Task();

		public Task getTask() {
			return task;
		}

		public class Task{

			@SerializedName("id")
			private String id = "";

			@SerializedName("count")
			private String count = "0";

			@SerializedName("count_complete")
			private String countComplete = "0";

			public String getId() {
				return id;
			}

			public String getCount() {
				return count;
			}

			public String getCountComplete() {
				return countComplete;
			}
		}

	}

	public String getStatus() {
		return status;
	}

	public boolean getSuccess() {
		return success;
	}

	public Data getData() {
		return data;
	}
}
