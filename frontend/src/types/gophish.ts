export type CampaignSummary = {
	id: string;
	name: string;
	created_date: string;
	launch_date: string;
	send_by_date: string;
	completed_date: string;
	status: string;
	stats: {
		total: number;
		sent: number;
		opened: number;
		clicked: number;
		submitted_data: number;
		email_reported: number;
		error: number;
	};
}

export type Campaign = {
  id: number;
  name: string;
  created_date: string;
  launch_date: string;
  send_by_date: string;
  completed_date: string;
  template: {
	id: number;
	name: string;
	subject: string;
	text: string;
	html: string;
	modified_date: string;
	attachments: any[];
  };
  page: {
	id: number;
	name: string;
	html: string;
	capture_credentials: boolean;
	capture_passwords: boolean;
	redirect_url: string;
	modified_date: string;
  };
  status: string;
  timeline: {
	email: string;
	time: string;
	message: string;
	details: string;
  }[];
}

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
