export type Template = {
	id: number;
	name: string;
	subject: string;
	text: string;
	html: string;
	modified_date: string;
};

export type Page = {
	id: number;
	name: string;
	html: string;
	capture_credentials: boolean;
	capture_passwords: boolean;
	modified_date: string;
	redirect_url: string;
};
