export class Category {

    categoryId: number;
    name: string;
    description: string;
    parentCategoryEntity: Category;

    constructor(categoryId?: number, name?: string, description?: string) {
        this.categoryId = categoryId;
        this.name = name;
    }
}
