import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPiloto, Piloto } from '../piloto.model';
import { PilotoService } from '../service/piloto.service';

@Injectable({ providedIn: 'root' })
export class PilotoRoutingResolveService implements Resolve<IPiloto> {
  constructor(protected service: PilotoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPiloto> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((piloto: HttpResponse<Piloto>) => {
          if (piloto.body) {
            return of(piloto.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Piloto());
  }
}
