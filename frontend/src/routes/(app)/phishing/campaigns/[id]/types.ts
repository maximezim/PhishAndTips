// src/routes/phishing/[id]/+types.ts
export type PageParams = {
    id: string;
};

export type PageLoad = (input: { params: PageParams }) => {
  title: string;
  dash_text: string;
  dash_bg: string;
  about_text: string;
  about_bg: string;
  form_text: string;
  form_bg: string;
  osint_text: string;
  osint_bg: string;
  pwd_text: string;
  pwd_bg: string;
  phishing_text: string;
  phishing_bg: string;
};

