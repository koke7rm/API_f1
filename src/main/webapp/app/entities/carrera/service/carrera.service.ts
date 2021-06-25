import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICarrera, getCarreraIdentifier } from '../carrera.model';

export type EntityResponseType = HttpResponse<ICarrera>;
export type EntityArrayResponseType = HttpResponse<ICarrera[]>;

@Injectable({ providedIn: 'root' })
export class CarreraService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/carreras');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(carrera: ICarrera): Observable<EntityResponseType> {
    return this.http.post<ICarrera>(this.resourceUrl, carrera, { observe: 'response' });
  }

  update(carrera: ICarrera): Observable<EntityResponseType> {
    return this.http.put<ICarrera>(`${this.resourceUrl}/${getCarreraIdentifier(carrera) as number}`, carrera, { observe: 'response' });
  }

  partialUpdate(carrera: ICarrera): Observable<EntityResponseType> {
    return this.http.patch<ICarrera>(`${this.resourceUrl}/${getCarreraIdentifier(carrera) as number}`, carrera, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICarrera>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICarrera[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCarreraToCollectionIfMissing(carreraCollection: ICarrera[], ...carrerasToCheck: (ICarrera | null | undefined)[]): ICarrera[] {
    const carreras: ICarrera[] = carrerasToCheck.filter(isPresent);
    if (carreras.length > 0) {
      const carreraCollectionIdentifiers = carreraCollection.map(carreraItem => getCarreraIdentifier(carreraItem)!);
      const carrerasToAdd = carreras.filter(carreraItem => {
        const carreraIdentifier = getCarreraIdentifier(carreraItem);
        if (carreraIdentifier == null || carreraCollectionIdentifiers.includes(carreraIdentifier)) {
          return false;
        }
        carreraCollectionIdentifiers.push(carreraIdentifier);
        return true;
      });
      return [...carrerasToAdd, ...carreraCollection];
    }
    return carreraCollection;
  }
}
