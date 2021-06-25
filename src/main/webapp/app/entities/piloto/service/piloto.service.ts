import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPiloto, getPilotoIdentifier } from '../piloto.model';

export type EntityResponseType = HttpResponse<IPiloto>;
export type EntityArrayResponseType = HttpResponse<IPiloto[]>;

@Injectable({ providedIn: 'root' })
export class PilotoService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/pilotos');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(piloto: IPiloto): Observable<EntityResponseType> {
    return this.http.post<IPiloto>(this.resourceUrl, piloto, { observe: 'response' });
  }

  update(piloto: IPiloto): Observable<EntityResponseType> {
    return this.http.put<IPiloto>(`${this.resourceUrl}/${getPilotoIdentifier(piloto) as number}`, piloto, { observe: 'response' });
  }

  partialUpdate(piloto: IPiloto): Observable<EntityResponseType> {
    return this.http.patch<IPiloto>(`${this.resourceUrl}/${getPilotoIdentifier(piloto) as number}`, piloto, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPiloto>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPiloto[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPilotoToCollectionIfMissing(pilotoCollection: IPiloto[], ...pilotosToCheck: (IPiloto | null | undefined)[]): IPiloto[] {
    const pilotos: IPiloto[] = pilotosToCheck.filter(isPresent);
    if (pilotos.length > 0) {
      const pilotoCollectionIdentifiers = pilotoCollection.map(pilotoItem => getPilotoIdentifier(pilotoItem)!);
      const pilotosToAdd = pilotos.filter(pilotoItem => {
        const pilotoIdentifier = getPilotoIdentifier(pilotoItem);
        if (pilotoIdentifier == null || pilotoCollectionIdentifiers.includes(pilotoIdentifier)) {
          return false;
        }
        pilotoCollectionIdentifiers.push(pilotoIdentifier);
        return true;
      });
      return [...pilotosToAdd, ...pilotoCollection];
    }
    return pilotoCollection;
  }
}
