export class Category {

    categoryId: number;
    name: string;
    description: string;
    parentCategoryEntity: Category;
    subCategoryEntities: Category[];

    constructor(categoryId?: number, name?: string, description?: string, subCategoryEntities?: Category[]) {
        this.categoryId = categoryId;
        this.name = name;
        this.subCategoryEntities = subCategoryEntities;
    }
}
