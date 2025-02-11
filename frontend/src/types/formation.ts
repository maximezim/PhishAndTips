export type Video = {
	id: number;
	title: string;
	description: string;
	videoUrl: string;
	captionUrl: string;
	thumbnailUrl: string;
};

export type Quiz = {
	id: number;
	title: string;
	description: string;
	json: string;
};
