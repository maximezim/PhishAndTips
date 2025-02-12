export type User = {
	firstName: string;
	lastName: string;
	email: string;
	position: string;
	role: string;
};

export type UserWithoutRole = {
	firstName: string;
	lastName: string;
	email: string;
	position: string;
};
