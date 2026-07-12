import { api } from './client';
import type { CategoryDetailsDTO, CategoryCreateDTO, CategoryUpdateDTO } from '../models';

const BASE = '/categories';

export const categoryApi = {
  findAll: () => api.get<CategoryDetailsDTO[]>(BASE).then((r) => r.data),

  findById: (id: number) =>
    api.get<CategoryDetailsDTO>(`${BASE}/${id}`).then((r) => r.data),

  create: (dto: CategoryCreateDTO) =>
    api.post<CategoryDetailsDTO>(BASE, dto).then((r) => r.data),

  update: (id: number, dto: CategoryUpdateDTO) =>
    api.put<CategoryDetailsDTO>(`${BASE}/${id}`, dto).then((r) => r.data),

  remove: (id: number) => api.delete(`${BASE}/${id}`),
};
