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

export type UserPagination = {
	users: User[];
	page: {
        size: number;
        totalElements: number;
        totalPages: number;
        number: number;
    };
}
