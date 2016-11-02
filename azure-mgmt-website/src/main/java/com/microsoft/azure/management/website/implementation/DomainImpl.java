/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 */

package com.microsoft.azure.management.website.implementation;

import com.microsoft.azure.Page;
import com.microsoft.azure.management.resources.fluentcore.arm.models.implementation.GroupableResourceImpl;
import com.microsoft.azure.management.resources.fluentcore.utils.Utils;
import com.microsoft.azure.management.website.Contact;
import com.microsoft.azure.management.website.Domain;
import com.microsoft.azure.management.website.DomainContact;
import com.microsoft.azure.management.website.DomainPurchaseConsent;
import com.microsoft.azure.management.website.DomainStatus;
import com.microsoft.azure.management.website.HostName;
import com.microsoft.azure.management.website.HostNameBinding;
import org.joda.time.DateTime;
import rx.Observable;
import rx.functions.Func1;
import rx.functions.FuncN;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The implementation for {@link Domain}.
 */
class DomainImpl
        extends
        GroupableResourceImpl<
                Domain,
                DomainInner,
                DomainImpl,
                AppServiceManager>
        implements
        Domain,
        Domain.Definition,
        Domain.Update {

    private final DomainsInner client;
    private final TopLevelDomainsInner topLevelDomainsInner;
    private final WebAppsInner webAppsInner;

    private Map<String, HostNameBindingImpl> hostNameBindingsToCreate;

    DomainImpl(String name, DomainInner innerObject, final DomainsInner client, final TopLevelDomainsInner topLevelDomainsInner, final WebAppsInner webAppsInner, AppServiceManager manager) {
        super(name, innerObject, manager);
        this.client = client;
        this.topLevelDomainsInner = topLevelDomainsInner;
        this.webAppsInner = webAppsInner;
        hostNameBindingsToCreate = new HashMap<>();
        inner().withLocation("global");
    }

    @Override
    public Observable<Domain> createResourceAsync() {
        String[] domainParts = this.name().split("\\.");
        String topLevel = domainParts[domainParts.length - 1];
        return topLevelDomainsInner.listAgreementsAsync(topLevel)
                // Step 1: Consent to agreements
                .flatMap(new Func1<Page<TldLegalAgreementInner>, Observable<List<String>>>() {
                    @Override
                    public Observable<List<String>> call(Page<TldLegalAgreementInner> tldLegalAgreementInnerPage) {
                        List<String> agreementKeys = new ArrayList<String>();
                        for (TldLegalAgreementInner agreementInner : tldLegalAgreementInnerPage.getItems()) {
                            agreementKeys.add(agreementInner.agreementKey());
                        }
                        return Observable.just(agreementKeys);
                    }
                })
                // Step 2: Create domain
                .flatMap(new Func1<List<String>, Observable<DomainInner>>() {
                    @Override
                    public Observable<DomainInner> call(List<String> keys) {
                        try {
                            inner().withConsent(new DomainPurchaseConsent()
                                    .withAgreedAt(new DateTime())
                                    .withAgreedBy(Inet4Address.getLocalHost().getHostAddress())
                                    .withAgreementKeys(keys));
                        } catch (UnknownHostException e) {
                            return Observable.error(e);
                        }
                        return client.createOrUpdateAsync(resourceGroupName(), name(), inner());
                    }
                })
                .map(innerToFluentMap(this))
                // Step 3: Apply host name bindings
                .flatMap(new Func1<Domain, Observable<Domain>>() {
                    @Override
                    public Observable<Domain> call(final Domain domain) {
                        final List<Observable<HostNameBinding>> hnbObservables = new ArrayList<>();
                        for (HostNameBindingImpl binding : hostNameBindingsToCreate.values()) {
                            binding.inner().withLocation("global");
                            hnbObservables.add(binding.createAsync());
                        }
                        return Observable.zip(hnbObservables, new FuncN<Domain>() {
                            @Override
                            public Domain call(Object... args) {
                                hostNameBindingsToCreate.clear();
                                return domain;
                            }
                        });
                    }
                });
    }

    @Override
    public DomainImpl refresh() {
        this.setInner(client.get(resourceGroupName(), name()));
        return this;
    }

    @Override
    public Contact adminContact() {
        return inner().contactAdmin();
    }

    @Override
    public Contact billingContact() {
        return inner().contactBilling();
    }

    @Override
    public Contact registrantContact() {
        return inner().contactRegistrant();
    }

    @Override
    public Contact techContact() {
        return inner().contactTech();
    }

    @Override
    public DomainStatus registrationStatus() {
        return inner().registrationStatus();
    }

    @Override
    public List<String> nameServers() {
        return inner().nameServers();
    }

    @Override
    public boolean privacy() {
        return Utils.toPrimitiveBoolean(inner().privacy());
    }

    @Override
    public DateTime createdTime() {
        return inner().createdTime();
    }

    @Override
    public DateTime expirationTime() {
        return inner().expirationTime();
    }

    @Override
    public DateTime lastRenewedTime() {
        return inner().lastRenewedTime();
    }

    @Override
    public boolean autoRenew() {
        return Utils.toPrimitiveBoolean(inner().autoRenew());
    }

    @Override
    public boolean readyForDnsRecordManagement() {
        return Utils.toPrimitiveBoolean(inner().readyForDnsRecordManagement());
    }

    @Override
    public List<HostName> managedHostNames() {
        return inner().managedHostNames();
    }

    @Override
    public DomainPurchaseConsent consent() {
        return inner().consent();
    }

    @Override
    public DomainImpl withAdminContact(Contact contact) {
        inner().withContactAdmin(contact);
        return this;
    }

    @Override
    public DomainImpl withBillingContact(Contact contact) {
        inner().withContactBilling(contact);
        return this;
    }

    @Override
    public DomainImpl withRegistrantContact(Contact contact) {
        inner().withContactAdmin(contact);
        inner().withContactBilling(contact);
        inner().withContactRegistrant(contact);
        inner().withContactTech(contact);
        return this;
    }

    @Override
    public DomainContact.DefinitionStages.Blank<Domain.DefinitionStages.WithCreate> defineRegistrantContact() {
        return new DomainContactImpl(new Contact(), this);
    }

    @Override
    public DomainImpl withTechContact(Contact contact) {
        inner().withContactTech(contact);
        return this;
    }

    @Override
    public DomainImpl withDomainPrivacyEnabled(boolean domainPrivacy) {
        inner().withPrivacy(domainPrivacy);
        return this;
    }

    @Override
    public DomainImpl withAutoRenewEnabled(boolean autoRenew) {
        inner().withAutoRenew(autoRenew);
        return this;
    }
}