import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICarrera, Carrera } from '../carrera.model';
import { CarreraService } from '../service/carrera.service';

@Injectable({ providedIn: 'root' })
export class CarreraRoutingResolveService implements Resolve<ICarrera> {
  constructor(protected service: CarreraService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICarrera> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((carrera: HttpResponse<Carrera>) => {
          if (carrera.body) {
            return of(carrera.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Carrera());
  }
}
